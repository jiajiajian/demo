package cn.com.tiza.service;


import cn.com.tiza.annotation.BusinessLogAnnotation;
import cn.com.tiza.api.GrampusApiClient;
import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dao.*;
import cn.com.tiza.domain.*;
import cn.com.tiza.dto.RestResult;
import cn.com.tiza.dto.UserType;
import cn.com.tiza.excel.ExcelWriter;
import cn.com.tiza.excel.read.ExcelReader;
import cn.com.tiza.service.dto.*;
import cn.com.tiza.service.mapper.VehicleMapper;
import cn.com.tiza.service.mapper.VehicleRealtimeMapper;
import cn.com.tiza.util.LocalDateTimeUtils;
import cn.com.tiza.web.rest.AccountApiClient;
import cn.com.tiza.web.rest.FenceVehicleClient;
import cn.com.tiza.web.rest.SimcardClient;
import cn.com.tiza.web.rest.errors.BadRequestException;
import cn.com.tiza.web.rest.errors.ErrorConstants;
import cn.com.tiza.web.rest.vm.*;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class VehicleService {
    private final String CONDITION_MSG = "CONDITION_MSG";
    private final String WORK_VEHICLE_LOC_DATA = "WORK_VEHICLE_LOC_DATA";
    private final String WORK_TIME = "WORK_TIME";
    private final String WORK_VEHICLE_DIRE = "WORK_VEHICLE_DIRE";
    private final String LOC_DATA_PROVINCE = "LOC_DATA_PROVINCE";
    private final String LOC_DATA_CITY = "LOC_DATA_CITY";
    private final String LOC_DATA_DISTRICT = "LOC_DATA_DISTRICT";
    private final String LOCATION = "LOCATION";
    private final String STA_LOC = "STA_LOC";
    private final String STATUS_INFO = "STATUS_INFO";
    private final String WORK_VEHICLE_SPEED = "WORK_VEHICLE_SPEED";
    private final String DIRE = "DIRE";

    @Autowired
    private VehicleDao vehicleDao;

    @Autowired
    private VehicleRealtimeDao vehicleRealtimeDao;

    @Autowired
    private VehicleMapper vehicleMapper;

    @Autowired
    private GrampusApiClient grampusApiClient;

    @Autowired
    private GrampusApiServiceImp apiServiceImp;

    @Autowired
    private SimcardClient simcardClient;

    @Autowired
    private BusinessLogDao businessLogDao;

    @Autowired
    private VehicleDebugDao vehicleDebugDao;

    @Autowired
    private RedisQueryService redisQueryService;

    @Autowired
    private VehicleRealtimeMapper realtimeMapper;

    @Autowired
    private FenceVehicleClient fenceVehicleClient;

    @Autowired
    private GrampusDataQueryService hbaseService;

    @Autowired
    private OrganizationDao organizationDao;

    @Autowired
    private BaseDicItemDao dicItemDao;

    @Autowired
    private VehicleTypeService vehicleTypeService;

    @Autowired
    private VehicleModelService vehicleModelService;

    @Autowired
    private AccountApiClient accountApiClient;


    public PageQuery<VehicleVM> findAll(VehicleQuery query) {
        query.formatServiceDateMil();
        if (Objects.isNull(query.getOrganizationId())) {
            query.setOrganizationId(BaseContextHandler.getOrgId());
        }
        query.setFinanceId(BaseContextHandler.getFinanceId());
        PageQuery<VehicleVM> pageQuery = query.toPageQuery();
        vehicleDao.pageQuery(pageQuery);
        List<VehicleVM> list = pageQuery.getList();
        if (list.isEmpty()) {
            return pageQuery;
        }
        List<String> vinList = list.stream().map(VehicleVM::getVin).collect(Collectors.toList());
        query.setVinList(vinList);
        if (Objects.nonNull(query.getCode())) {
            query.setCode("%" + query.getCode() + "%");
        }
        List<VehicleLockItemVM> vehicleLockList = vehicleDao.vehicleLockList(query);
        /*if (vehicleLockList.isEmpty()) {
            return pageQuery;
        }*/
        //解析锁车状态
        //将锁车配置转换 vin -> 配置 方便取值
        Map<String, List<VehicleLockItemVM>> lockGroup = vehicleLockList
                .stream()
                .collect(Collectors.groupingBy(VehicleLockItemVM::getVin));
        pageQuery.getList().forEach(vehicleVM -> {
            //将累计工作时长由毫秒换算为小时
            BigDecimal totalWorkTime = vehicleVM.getTotalWorkTime();
            if (Objects.nonNull(totalWorkTime)) {
                BigDecimal divide = totalWorkTime.divide(BigDecimal.valueOf(3600), 2, BigDecimal.ROUND_HALF_UP);
                vehicleVM.setTotalWorkTime(divide);
            }

            //获取每个车的锁车配置
            List<VehicleLockItemVM> vehicleLockItems = lockGroup.get(vehicleVM.getVin());
            if (Objects.isNull(vehicleLockItems)) {
                return;
            }
            //每辆车对应 lock配置项map
            Map<String, VehicleLockItemVM> itemMap = vehicleLockItems.stream()
                    .collect(Collectors.toMap(VehicleLockItemVM::getItemCode, v -> v));
            setLockStatus(vehicleVM, itemMap);
        });
        return pageQuery;
    }

    /**
     * @param lockVM
     * @param itemMap 车辆锁车配置列表
     */
    private void setLockStatus(LockVM lockVM, Map<String, VehicleLockItemVM> itemMap) {
        HashMap<String, Integer> lockStatus = Maps.newHashMap();
        lockVM.setLockStatus(lockStatus);
        if (Objects.nonNull(itemMap.get(LockEnum.CONTROLLER_LOCK.name()))) {
            lockStatus.put(LockEnum.CONTROLLER_LOCK.name(), null == lockVM.getLock() ? 0 : lockVM.getLock());
        }
        if (Objects.nonNull(itemMap.get(LockEnum.RELAY_LOCK_1.name()))) {
            lockStatus.put(LockEnum.RELAY_LOCK_1.name(), null == lockVM.getOneLevelLock() ? 0 : lockVM.getOneLevelLock());
        }
        if (Objects.nonNull(itemMap.get(LockEnum.RELAY_LOCK_2.name()))) {
            lockStatus.put(LockEnum.RELAY_LOCK_2.name(), null == lockVM.getTwoLevelLock() ? 0 : lockVM.getTwoLevelLock());
        }
        if (Objects.nonNull(itemMap.get(LockEnum.RELAY_LOCK_3.name()))) {
            lockStatus.put(LockEnum.RELAY_LOCK_3.name(), null == lockVM.getThreeLevelLock() ? 0 : lockVM.getThreeLevelLock());
        }
    }

    public void checkBatchQueryVinList(ExcelReader reader, List<VehicleBatchQueryImportDto> dtos) {
        //当前用户权限下车辆列表
        List<String> vinList = vehicleDao.vinListByOrgIdOrFinanceId(BaseContextHandler.getOrgId(), BaseContextHandler.getFinanceId());
        //先校验vin是否存在
        for (int i = 0; i < dtos.size(); i++) {
            VehicleBatchQueryImportDto dto = dtos.get(i);
            if (!vinList.contains(dto.getVin())) {
                reader.addCellError(dto, VehicleServiceDateDto.IDX_VIN, "VIN不存在");
            }
        }
    }

    public List<VehicleVM> batchSearch(List<String> vinList) {
        if (Objects.isNull(vinList) || vinList.isEmpty()) {
            return null;
        }
        return vehicleDao.batchQuery(vinList);
    }

    public Optional<Vehicle> get(Long id) {
        return Optional.ofNullable(vehicleDao.single(id));
    }

    public Vehicle create(VehicleDto dto) {
        Vehicle entity = vehicleMapper.dtoToEntity(dto);
        long existCountVin = vehicleDao.vehicleCountByVin(dto.getVin());
        if(existCountVin > 0){
            throw new BadRequestException("error.vehicle.create.exist");
        }
        vehicleDao.insert(entity);
        //注册
        BaseDicItem dicItem = dicItemDao.single(dto.getDictId());
        RestResult register = this.register(dto.getVin(), dicItem);
        if (!Objects.equals(register.getIsSuccess(), Boolean.TRUE)) {
            log.error("register exception: {}", register.getErrorCode());
            throw new BadRequestException("register code is: " + register.getErrorCode());
        }
        VehicleRealtime vr = new VehicleRealtime();
        vr.setVin(dto.getVin());
        vehicleRealtimeDao.insert(vr);
        return entity;
    }

    public Optional<Vehicle> update(Long id, VehicleDto command) {
        return get(id).map(entity -> {
            //若更换组织 不可跨越一级组织 删除旧组织的所有围栏关联了此车的数据删除
            if (Objects.nonNull(command.getOrganizationId()) &&
                    !entity.getOrganizationId().equals(command.getOrganizationId())) {
                Long rootId1 = vehicleDao.findRootOrgByOrg(command.getOrganizationId());
                Long rootId2 = vehicleDao.findRootOrgByOrg(entity.getOrganizationId());
                if (!rootId1.equals(rootId2)) {
                    throw new BadRequestException(ErrorConstants.CAN_NOT_CHANGE_ROOT_ORG);
                }
                fenceVehicleClient.deleteRelatedVehiclesByOrgId(entity.getVin(), entity.getOrganizationId());
            }
            entity.setVehicleTypeId(command.getVehicleTypeId());
            entity.setVehicleModelId(command.getVehicleModelId());
            entity.setOrganizationId(command.getOrganizationId());
            entity.setUpdateTime(System.currentTimeMillis());
            entity.setUpdateUserAccount(BaseContextHandler.getLoginName());
            entity.setUpdateUserRealname(BaseContextHandler.getName());
            vehicleDao.updateById(entity);
            return entity;
        });
    }

    @BusinessLogAnnotation(BusinessLogType.VEHICLE_DELETE)
    public List<VehicleTerminalVM> delete(Long id) {
        ArrayList<VehicleTerminalVM> list = Lists.newArrayList();
        //只能删除服务状态为 未开通的车辆
        get(id).ifPresent(vehicle -> {
            if (Objects.nonNull(vehicle.getServiceStatus()) && !vehicle.getServiceStatus().equals(ServiceStatus.NOT_OPEN)) {
                throw new BadRequestException(ErrorConstants.VEHICLE_SERVICE_STATUS_CAN_NOT_DELETE);
            }
            VehicleTerminalVM vehicleTerminalInfo = vehicleDao.vehicleAndTerminalInfoByVin(vehicle.getVin());
            list.add(vehicleTerminalInfo);
            unregister(vehicle.getVin());
            boolean result = simcardClient.resetCardStatusAndDeleteTerminal(vehicle.getTerminalId(), 0);
            if (result) {
                vehicleDao.deleteById(id);
                vehicleRealtimeDao.createLambdaQuery().andEq(VehicleRealtime::getVin, vehicle.getVin())
                        .delete();
                //删除redis对应key
                redisQueryService.delete(vehicle.getVin());
                //删除报警数据
                fenceVehicleClient.deleteByVin(vehicle.getVin());
                //删除业务日志
                /*businessLogDao.createLambdaQuery().andEq(BusinessLog::getVin, vehicle.getVin())
                        .delete();*/
            } else {
                throw new BadRequestException(ErrorConstants.FAIL_TO_DELETE_TERMINAL);
            }
        });
        return list;
    }


    public void delete(Long[] ids) {
        for (Long id : ids) {
            get(id).ifPresent(vehicle -> {
                if (Objects.nonNull(vehicle.getServiceStatus()) && !vehicle.getServiceStatus().equals(ServiceStatus.NOT_OPEN)) {
                    throw new BadRequestException(ErrorConstants.VEHICLE_SERVICE_STATUS_CAN_NOT_DELETE);
                }
            });
        }
        for (Long id : ids) {
            delete(id);
        }
    }

    public void updateSaleInfo(Long id, VehicleSaleDto saleDto) {
        get(id).ifPresent(vehicle -> {
            vehicle.setSaleStatus(saleDto.getSaleStatus());
            vehicle.setSaleMethod(saleDto.getSaleMethod());
            vehicle.setContractNumber(saleDto.getContractNumber());
            vehicle.setSaleDate(saleDto.getSaleDate());
            vehicle.setLoanPeriod(saleDto.getLoanPeriod());
            vehicle.setSeller(saleDto.getSeller());
            vehicle.setCustomerId(saleDto.getCustomerId());
            vehicle.setFinanceId(saleDto.getFinanceId());
            vehicleDao.updateById(vehicle);
        });
    }

    @BusinessLogAnnotation(BusinessLogType.EXTEND_SERVICE)
    public List<VehicleTerminalVM> updateServiceInfo(Long id, VehicleServiceDto serviceDto) {
        ArrayList<VehicleTerminalVM> returnList = Lists.newArrayList();
        Vehicle vehicle = vehicleDao.single(id);
        if (Objects.isNull(vehicle)) {
            return returnList;
        }
        int endDate = Integer.parseInt(LocalDateTimeUtils.formatDay(serviceDto.getServiceEndDate(), "yyyyMMdd"));
        VehicleTerminalVM vehicleTerminalVM = vehicleDao.vehicleAndTerminalInfoByVin(vehicle.getVin());
        //只有服务期延长了才记录日志 延长月份为endDate之差的月数
        if (Objects.nonNull(vehicle.getServiceEndDate()) && vehicle.getServiceEndDate() < endDate) {
            int monthDuration = LocalDateTimeUtils.monthDuration(LocalDateTimeUtils.DATE_PATTERN_A, vehicle.getServiceEndDate() + "", endDate + "");
            vehicleTerminalVM.setRenewalMon(monthDuration);
            returnList.add(vehicleTerminalVM);
        }
        vehicle.setServiceStartDate(Integer.parseInt(LocalDateTimeUtils.formatDay(serviceDto.getServiceStartDate(), "yyyyMMdd")));
        vehicle.setServiceEndDate(endDate);
        vehicle.setServicePeriod(serviceDto.getServicePeriod());
        vehicle.setServiceStatus(ServiceStatus.fromValue(serviceDto.getServiceStatus()));
        vehicleDao.updateById(vehicle);
        return returnList;
    }

    public DebugDateVM getDebugDate(String vin) {
        return vehicleDao.getDebugDate(vin);
    }

    public void updateDebugDate(String vin, Long endTime) {
        Optional.ofNullable(vehicleDebugDao.createLambdaQuery()
                .andEq(VehicleDebug::getVin, vin)
                .single())
                .map(debug -> {
                    if (endTime < debug.getDebugBeginTime()) {
                        throw new BadRequestException(ErrorConstants.DEBUG_END_TIME_LESS_THAN_START_TIME);
                    }
                    //若结束时间大于当前月的开始时间 将调试状态设为可调式
                    if (endTime > LocalDateTimeUtils.getMonStartDayTime()) {
                        debug.setTestStatus(1);
                    }
                    debug.setDebugEndTime(endTime);
                    vehicleDebugDao.updateById(debug);
                    return debug;
                }).orElseThrow(() -> new BadRequestException(ErrorConstants.VEHICLE_IS_NOT_IN_DEBUG));

    }

    public TerminalInfoVM findTerminalInfoByVin(String vin) {
        return Optional.ofNullable(vehicleDao.findTerminalInfoByVin(vin)).orElseGet(TerminalInfoVM::new);
    }

    public List<VehicleTerminalVM> changeTerminal(String vin, String newTerminalCode) {
        ArrayList<VehicleTerminalVM> returnList = Lists.newArrayList();
        Vehicle vehicle = vehicleDao.createLambdaQuery().andEq(Vehicle::getVin, vin).single();
        if (Objects.isNull(vehicle)) {
            throw new BadRequestException(ErrorConstants.VEHICLE_NOT_EXIST);
        }
        checkVinIsVirtual(vin);
        TerminalInfoVM oldTerminalInfo = vehicleDao.findTerminalById(vehicle.getTerminalId());
        TerminalInfoVM newTerminalInfo = vehicleDao.findTerminalByCode(newTerminalCode);

        if (Objects.isNull(newTerminalInfo)) {
            throw new BadRequestException(ErrorConstants.TERMINAL_NOT_EXIST);
        }
        //目标终端对应车辆信息 目标必然会有对应车辆信息，车辆vin为虚拟机器序列号
        Vehicle single = vehicleDao.createLambdaQuery().andEq(Vehicle::getTerminalId, newTerminalInfo.getTerminalId()).single();
        if (Objects.isNull(single)) {
            throw new BadRequestException("terminal.notHave_virtual_vin,please check this terminal");
        }
        //目标终端 和 原终端 不可跨越一级机构
        Long rootId1 = vehicleDao.findRootOrgByOrg(vehicle.getOrganizationId());
        Long rootId2 = vehicleDao.findRootOrgByOrg(single.getOrganizationId());
        if (!rootId1.equals(rootId2)) {
            throw new BadRequestException(ErrorConstants.CAN_NOT_CHANGE_ROOT_ORG);
        }
        //当新终端对应的车辆的机器序列号为虚拟机器序列号时，说明终端未被绑定至真正车辆，可以作为目标终端被更换
        if (!single.getVin().equals(newTerminalInfo.getTerminalCode())) {
            throw new BadRequestException(ErrorConstants.TERMINAL_HAS_RELATION_WITH_REAL_VEHICLE);
        }
        vehicle.setTerminalId(newTerminalInfo.getTerminalId());
        vehicleDao.updateById(vehicle);

        //tstar将旧终端对应虚拟机器序列号取消注册 删除车辆表/实时表对应信息
        unregister(single.getVin());
        redisQueryService.delete(single.getVin());
        vehicleDao.createLambdaQuery().andEq(Vehicle::getId, single.getId()).delete();
        vehicleRealtimeDao.createLambdaQuery().andEq(VehicleRealtime::getVin, single.getVin()).delete();

        //将原终端作为基础信息 新增至车辆表，机器序列号为虚拟机器序列号，注册至tstar，组织为原车一级机构,其他信息清除
        Vehicle newVehicle = new Vehicle();
        newVehicle.setOrganizationId(rootId1);
        newVehicle.setTerminalId(oldTerminalInfo.getTerminalId());
        newVehicle.setVin(oldTerminalInfo.getTerminalCode());
        vehicleDao.insert(newVehicle);
        VehicleRealtime vehicleRealtime = new VehicleRealtime();
        vehicleRealtime.setVin(newVehicle.getVin());
        vehicleRealtimeDao.insert(vehicleRealtime);
        register(newVehicle.getVin(), vehicleDao.findProtocolTypeAndApiKey(vin));
        //新增更换终端日志
        VehicleTerminalVM vehicleTerminalVM = new VehicleTerminalVM();
        vehicleTerminalVM.setVin(vin);
        vehicleTerminalVM.setOldTerminal(oldTerminalInfo.getTerminalCode());
        vehicleTerminalVM.setOldSimCard(oldTerminalInfo.getSimCard());
        vehicleTerminalVM.setTerminalCode(newTerminalInfo.getTerminalCode());
        vehicleTerminalVM.setSimCard(newTerminalInfo.getSimCard());
        vehicleTerminalVM.setOrganizationId(vehicle.getOrganizationId());
        returnList.add(vehicleTerminalVM);
        return returnList;
    }

    /**
     * 根据vin校验当前车辆是否是虚拟机器序列号
     *
     * @param vin
     */
    private void checkVinIsVirtual(String vin) {
        VehicleTerminalVM vehicleTerminalVM = vehicleDao.vehicleAndTerminalInfoByVin(vin);
        if (vehicleTerminalVM.getTerminalCode().equals(vehicleTerminalVM.getVin())) {
            throw new BadRequestException(ErrorConstants.VIRTUAL_VIN_CAN_NOT_CHANGE_TERMINAL);
        }
    }

    /**
     * @param vin
     * @param organizationId
     * @param oldTerminalInfo 旧终端
     * @param newTerminalInfo 新终端
     */
    private void saveChangeTerminalLog(String vin, Long organizationId, TerminalInfoVM oldTerminalInfo, TerminalInfoVM newTerminalInfo) {
        BusinessLog businessLog = BusinessLog.builder().operateType(BusinessLogType.CHANGE_TERMINAL)
                .createUserAccount(BaseContextHandler.getLoginName())
                .createUserRealname(BaseContextHandler.getName())
                .createTime(System.currentTimeMillis())
                .oldSimcard(oldTerminalInfo.getSimCard())
                .oldTerminal(oldTerminalInfo.getTerminalCode())
                .simcard(newTerminalInfo.getSimCard())
                .terminal(newTerminalInfo.getTerminalCode())
                .organizationId(organizationId)
                .vin(vin)
                .build();
        businessLogDao.insert(businessLog);
    }


    public void swapTerminal(String vin1, String vin2) {
        Vehicle vehicle1 = vehicleDao.createLambdaQuery().andEq(Vehicle::getVin, vin1).single();
        Vehicle vehicle2 = vehicleDao.createLambdaQuery().andEq(Vehicle::getVin, vin2).single();
        if (Objects.isNull(vehicle1) || Objects.isNull(vehicle2)) {
            return;
        }
        checkVinIsVirtual(vin1);
        checkVinIsVirtual(vin2);
        //管理员不可跨越一级机构互换终端 普通用户只可更换自己权限范围内终端
        if (BaseContextHandler.getUserType().equals(UserType.ADMIN.name())) {
            Long root1 = vehicleDao.findRootOrgByOrg(vehicle1.getOrganizationId());
            Long root2 = vehicleDao.findRootOrgByOrg(vehicle2.getOrganizationId());
            if (!root1.equals(root2)) {
                throw new BadRequestException(ErrorConstants.CAN_NOT_CHANGE_ROOT_ORG);
            }
        } else {
            if (Objects.isNull(vehicleDao.checkVinByOrgAndVin(BaseContextHandler.getOrgId(), vin2))) {
                throw new BadRequestException(ErrorConstants.NOT_HAVE_PERMISSION_FOR_VIN);
            }
        }
        TerminalInfoVM terminal1 = vehicleDao.findTerminalById(vehicle1.getTerminalId());
        TerminalInfoVM terminal2 = vehicleDao.findTerminalById(vehicle2.getTerminalId());
        Long terminalId1 = vehicle1.getTerminalId();
        vehicle1.setTerminalId(vehicle2.getTerminalId());
        vehicle2.setTerminalId(terminalId1);
        vehicleDao.updateById(vehicle1);
        vehicleDao.updateById(vehicle2);
        //记录日志
        saveChangeTerminalLog(vin1, vehicle1.getOrganizationId(), terminal1, terminal2);
        saveChangeTerminalLog(vin2, vehicle2.getOrganizationId(), terminal2, terminal1);
    }


    @BusinessLogAnnotation(value = BusinessLogType.MOVE)
    public List<VehicleTerminalVM> moveVin(String oldVin, String newVin) {
        ArrayList<VehicleTerminalVM> returnList = Lists.newArrayList();
        VehicleTerminalVM vehicleTerminalVM = vehicleDao.vehicleAndTerminalInfoByVin(oldVin);
        if (Objects.isNull(vehicleTerminalVM)) {
            throw new BadRequestException(ErrorConstants.ENTITY_NOT_FOUND_TYPE);
        }
        //车辆导入之前 即 未更新真实机器序列号之前 不可移机
        if (vehicleTerminalVM.getTerminalCode().equals(vehicleTerminalVM.getVin())) {
            throw new BadRequestException(ErrorConstants.VIRTUAL_VIN_CAN_NOT_MOVE);
        }
        long count = vehicleDao.createLambdaQuery().andEq(Vehicle::getVin, newVin).count();
        if (count > 0) {
            throw new BadRequestException(ErrorConstants.VIN_HAS_EXIST);
        }
        Vehicle vehicle = vehicleDao.createLambdaQuery().andEq(Vehicle::getVin, oldVin).single();
        if (Objects.isNull(vehicle)) {
            return returnList;
        }
        //tstar先删除 再重新注册
        RestResult unregisterResult = unregister(oldVin);
        if (!unregisterResult.getIsSuccess()) {
            throw new BadRequestException(unregisterResult.getErrorCode() + "");
        }
        redisQueryService.delete(oldVin);
        RestResult registerResult = register(newVin, vehicleDao.findProtocolTypeAndApiKey(oldVin));
        if (!registerResult.getIsSuccess()) {
            throw new BadRequestException(registerResult.getErrorCode() + "");
        }
        vehicle.setVin(newVin);
        vehicleDao.updateById(vehicle);
        //realtime表更新对应vin
        VehicleRealtime vehicleRealtime = vehicleRealtimeDao.createLambdaQuery().andEq(VehicleRealtime::getVin, oldVin).single();
        if (Objects.nonNull(vehicleRealtime)) {
            vehicleRealtime.setVin(newVin);
            vehicleRealtimeDao.updateById(vehicleRealtime);
        }
        vehicleTerminalVM.setOldVin(oldVin);
        vehicleTerminalVM.setVin(newVin);
        returnList.add(vehicleTerminalVM);
        return returnList;
    }


    /**
     * 所有涉及更新车辆操作向tstar注册车辆
     *
     * @param vin
     * @param protocolType 根据终端查出对应协议的apikey和协议类型对象
     * @return
     */
    private RestResult register(String vin, ProtocolVM protocolType) {

        if (Objects.isNull(protocolType)) {
            throw new BadRequestException(ErrorConstants.VEHICLE_DO_NOT_HAVE_PROTOCAL_TYPE);
        }
        return grampusApiClient.register(protocolType.getApiKey(), protocolType.getProtocolType(), vin);
    }

    /**
     * 新增车辆时注册tstar方法
     *
     * @param vin
     * @param dicItem 协议类型对应字典表数据
     * @return
     */
    private RestResult register(String vin, BaseDicItem dicItem) {
        return grampusApiClient.register(dicItem.getRemark(), dicItem.getItemCode(), vin);
    }

    private RestResult unregister(String vin) {
        ProtocolVM protocolType = vehicleDao.findProtocolTypeAndApiKey(vin);
        if (Objects.nonNull(protocolType)) {
            return grampusApiClient.unregister(protocolType.getApiKey(), protocolType.getProtocolType(), vin);
        }
        RestResult restResult = new RestResult();
        restResult.setIsSuccess(true);
        return restResult;
    }

    /**
     * @param reader
     * @param dtos
     * @param vehicleList
     */
    public void checkUpdateServiceDateVin(ExcelReader reader, List<VehicleServiceDateDto> dtos, List<Vehicle> vehicleList) {
        //当前用户权限下车辆列表
        List<String> vinList = vehicleDao.vinListByOrgIdOrFinanceId(BaseContextHandler.getOrgId(), BaseContextHandler.getFinanceId());
        //先校验vin是否存在
        for (int i = 0; i < dtos.size(); i++) {
            VehicleServiceDateDto dto = dtos.get(i);
            if (!vinList.contains(dto.getVin())) {
                reader.addCellError(dto, VehicleServiceDateDto.IDX_VIN, "VIN不存在");
            }
        }
        // 校验服务是否开通
        if (null == vehicleList || vehicleList.isEmpty()) {
            return;
        }
        Map<String, Vehicle> vehicleMap = vehicleList.stream().collect(Collectors.toMap(Vehicle::getVin, v -> v, (k1, k2) -> k2));
        dtos.forEach(dto -> {
            Vehicle vehicle = vehicleMap.get(dto.getVin());
            if (Objects.nonNull(vehicle)) {
                if (Objects.isNull(vehicle.getServiceStatus()) || !vehicle.getServiceStatus().equals(ServiceStatus.HAS_OPENED)) {
                    reader.addCellError(dto, VehicleServiceDateDto.IDX_VIN, "车辆服务未开通");
                }
            }
        });
    }

    @BusinessLogAnnotation(BusinessLogType.EXTEND_SERVICE)
    public List<VehicleTerminalVM> updateServiceDate(List<VehicleServiceDateDto> list, List<Vehicle> vehicleList) {
        ArrayList<VehicleTerminalVM> returnList = Lists.newArrayList();
        if (list.isEmpty()) {
            return returnList;
        }
        Map<String, VehicleServiceDateDto> dtoMap = list.stream()
                .collect(Collectors.toMap(VehicleServiceDateDto::getVin, v -> v, (k1, k2) -> k2));
        vehicleList.forEach(vehicle -> {
            VehicleTerminalVM vehicleTerminalVM = vehicleDao.vehicleAndTerminalInfoByVin(vehicle.getVin());
            VehicleServiceDateDto dto = dtoMap.get(vehicle.getVin());
            vehicleTerminalVM.setRenewalMon(Integer.parseInt(dto.getPeriod()));
            returnList.add(vehicleTerminalVM);
            //设置服务期限
            int period = vehicle.getServicePeriod() == null ? Integer.parseInt(dto.getPeriod()) : vehicle.getServicePeriod() + Integer.parseInt(dto.getPeriod());
            vehicle.setServicePeriod(period);
            //设置服务结束日期
            String endDate = LocalDateTimeUtils.addMonth(Integer.parseInt(dto.getPeriod()), LocalDateTimeUtils.DATE_PATTERN_A, String.valueOf(vehicle.getServiceEndDate()));
            vehicle.setServiceEndDate(Integer.parseInt(endDate));
            vehicle.setDescription(dto.getDescription());
            vehicleDao.updateById(vehicle);
        });
        return returnList;
    }

    /**
     * @param reader
     * @param dtos
     * @param vehicleList
     * @param isRestore   true:暂停恢复 false:暂停
     */
    public void checkSuspendedVin(ExcelReader reader, List<VehicleServiceSuspendedImportDto> dtos, List<Vehicle> vehicleList, boolean isRestore) {
        //当前用户权限下车辆列表
        List<String> vinList = vehicleDao.vinListByOrgIdOrFinanceId(BaseContextHandler.getOrgId(), BaseContextHandler.getFinanceId());
        //先校验vin是否存在
        for (int i = 0; i < dtos.size(); i++) {
            VehicleServiceSuspendedImportDto dto = dtos.get(i);
            if (!vinList.contains(dto.getVin())) {
                reader.addCellError(dto, VehicleServiceDateDto.IDX_VIN, "VIN不存在");
            }
        }
        // 校验服务是否开通
        if (null == vehicleList || vehicleList.isEmpty()) {
            return;
        }
        Map<String, Vehicle> vehicleMap = vehicleList.stream().collect(Collectors.toMap(Vehicle::getVin, v -> v, (k1, k2) -> k2));
        dtos.forEach(dto -> {
            Vehicle vehicle = vehicleMap.get(dto.getVin());
            if (Objects.nonNull(vehicle)) {
                if (isRestore) {
                    if (Objects.isNull(vehicle.getServiceStatus()) || !(vehicle.getServiceStatus().equals(ServiceStatus.EXPIRE) || vehicle.getServiceStatus().equals(ServiceStatus.SUSPENDED))) {
                        reader.addCellError(dto, VehicleServiceSuspendedImportDto.IDX_VIN, "车辆服务不是到期或暂停状态");
                    }
                } else {
                    if (Objects.isNull(vehicle.getServiceStatus()) || !vehicle.getServiceStatus().equals(ServiceStatus.HAS_OPENED)) {
                        reader.addCellError(dto, VehicleServiceSuspendedImportDto.IDX_VIN, "车辆服务未开通");
                    }
                }

            }
        });
    }

    @BusinessLogAnnotation(BusinessLogType.SUSPEND)
    public List<VehicleTerminalVM> suspendedServiceStatus(List<Vehicle> vehicleList) {
        vehicleList.forEach(vehicle -> {
            vehicle.setServiceStatus(ServiceStatus.SUSPENDED);
            vehicleDao.updateById(vehicle);
        });
        return vehicleDao.vehicleAndTerminalInfoListByVinList(vehicleList.stream()
                .map(Vehicle::getVin).collect(Collectors.toList()));

    }

    @BusinessLogAnnotation(BusinessLogType.SUSPEND_RESTORE)
    public List<VehicleTerminalVM> restoreServiceStatus(List<Vehicle> vehicleList) {
        //如果服务状态为已到期，那么车辆对应的服务结束日期变更为执行日当天日期
        // 如果服务状态为已暂停，那么服务结束日期不变
        vehicleList.forEach(vehicle -> {
            if (vehicle.getServiceStatus().equals(ServiceStatus.EXPIRE)) {
                vehicle.setServiceEndDate(Integer.parseInt(LocalDateTimeUtils.getCurrentDay(LocalDateTimeUtils.DATE_PATTERN_A)));
                vehicle.setServicePeriod(LocalDateTimeUtils.monthDuration(LocalDateTimeUtils.DATE_PATTERN_A, vehicle.getServiceStartDate() + "", vehicle.getServiceEndDate() + ""));

            }
            vehicle.setServiceStatus(ServiceStatus.HAS_OPENED);
            vehicleDao.updateById(vehicle);
        });
        return vehicleDao.vehicleAndTerminalInfoListByVinList(vehicleList.stream()
                .map(Vehicle::getVin).collect(Collectors.toList()));
    }

    public void checkCloseAccountVin(ExcelReader reader, List<VehicleServiceSuspendedImportDto> dtos) {
        //当前用户权限下车辆列表
        List<String> vinList = vehicleDao.vinListByOrgIdOrFinanceId(BaseContextHandler.getOrgId(), BaseContextHandler.getFinanceId());
        //先校验vin是否存在
        for (int i = 0; i < dtos.size(); i++) {
            VehicleServiceSuspendedImportDto dto = dtos.get(i);
            if (!vinList.contains(dto.getVin())) {
                reader.addCellError(dto, VehicleServiceDateDto.IDX_VIN, "VIN不存在");
            }
        }
    }

    public List<String> findVehicleServiceIsOpened(List<Vehicle> vehicleList) {
        return vehicleList.stream()
                .filter(vehicle -> Objects.nonNull(vehicle.getServiceStatus()) && vehicle.getServiceStatus().equals(ServiceStatus.HAS_OPENED))
                .map(Vehicle::getVin)
                .collect(Collectors.toList());

    }

    @BusinessLogAnnotation(BusinessLogType.CLOSE_ACCOUNT)
    public List<VehicleTerminalVM> closeAccount(List<Vehicle> vehicleList) {
        ArrayList<VehicleTerminalVM> returnList = Lists.newArrayList();
        vehicleList.forEach(vehicle -> {
            RestResult unregister = unregister(vehicle.getVin());
            if (!unregister.getIsSuccess()) {
                throw new BadRequestException(unregister.getErrorCode() + "");
            }
            VehicleTerminalVM vehicleTerminalInfo = vehicleDao.vehicleAndTerminalInfoByVin(vehicle.getVin());
            boolean flag = simcardClient.resetCardStatusAndDeleteTerminal(vehicle.getTerminalId(), 4);
            if (!flag) {
                throw new BadRequestException(ErrorConstants.FAIL_TO_DELETE_TERMINAL);
            }
            vehicleDao.deleteById(vehicle.getId());
            vehicleRealtimeDao.createLambdaQuery()
                    .andEq(VehicleRealtime::getVin, vehicle.getVin())
                    .delete();
            returnList.add(vehicleTerminalInfo);
        });
        return returnList;
    }

    public void checkVehicleImport(ExcelReader reader, List<VehicleImportDto> dtoList) {
        //当前用户车辆终端信息列表
        List<VehicleTerminalVM> vehicleTerminalList = vehicleDao.vehicleTerminalListByOrgId(BaseContextHandler.getOrgId());
        //终端编号-SIM 对应map
        Map<String, VehicleTerminalVM> terminalSimMap = vehicleTerminalList.stream().collect(Collectors.toMap(VehicleTerminalVM::getTerminalCode, v -> v,(k1,k2) -> k2));
        //系统全部vin
        List<String> allVin = vehicleDao.findAllVin();
        //组织列表
        List<String> orgNameList = vehicleDao.findOrgList(BaseContextHandler.getOrgId()).stream().map(OrganizationVM::getOrgName).collect(Collectors.toList());

        dtoList.forEach(dto -> {
            if (Objects.nonNull(dto.getVin()) && Objects.nonNull(dto.getTerminalCode())
                    && dto.getVin().equals(dto.getTerminalCode())) {
                reader.addCellError(dto, VehicleImportDto.IDX_VIN, "机器序列号不可和终端编号一致");
            }

            if (allVin.contains(dto.getVin())) {
                reader.addCellError(dto, VehicleImportDto.IDX_VIN, "VIN在系统已经存在");
            }

            if (Objects.isNull(terminalSimMap.get(dto.getTerminalCode()))) {
                reader.addCellError(dto, VehicleImportDto.IDX_TERMINAL_CODE, "终端不存在");
                return;
            }

            if (Objects.nonNull(terminalSimMap.get(dto.getTerminalCode()))
                    && !terminalSimMap.get(dto.getTerminalCode()).getSimCard().equals(dto.getSimCard())) {
                reader.addCellError(dto, VehicleImportDto.IDX_TERMINAL_CODE, "终端和SIM卡不对应");
            }
            //若此时车辆表 vin 和 terminalcode 不一致，说明曾经导入过 不允许再次导入
            VehicleTerminalVM vehicleTerminalVM = terminalSimMap.get(dto.getTerminalCode());
            if (Objects.nonNull(vehicleTerminalVM)) {
                if (!vehicleTerminalVM.getTerminalCode().equals(vehicleTerminalVM.getVin())) {
                    reader.addCellError(dto, VehicleImportDto.IDX_VIN, "不可再次被导入");
                }
            }


            //校验机构 不可跨越一级机构
            if (!orgNameList.contains(dto.getOrganization())) {
                reader.addCellError(dto, VehicleImportDto.IDX_ORGANIZATION, "机构不存在");
                return;
            }
            //机构名称可能重复，出判断主要针对管理员
            if (!BaseContextHandler.getUserType().equals(UserType.ADMIN.name())) {
                Organization org = organizationDao.findOrgByRootIdAndName(BaseContextHandler.getRootOrgId(), dto.getOrganization());
                dto.setOrganizationId(org.getId());
            } else {
                //校验是否跨越一级机构
                List<Organization> orgList = organizationDao.findOrgByName(dto.getOrganization());
                //车辆目前在系统中对应组织的根组织
                Long rootId = vehicleDao.findRootOrgByOrg(terminalSimMap.get(dto.getTerminalCode()).getOrganizationId());
                //orgList size 必然大于0
                boolean flag = true;
                for (int i = 0; i < orgList.size(); i++) {
                    Organization organization = orgList.get(i);
                    if (organization.getRootOrgId().equals(rootId)) {
                        dto.setOrganizationId(organization.getId());
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    reader.addCellError(dto, VehicleImportDto.IDX_ORGANIZATION, "不可跨越一级机构");
                }
            }

        });
    }

    public void checkImportModel(ExcelReader reader, List<VehicleImportDto> dtoList) {
        dtoList.forEach(dto -> {
            Map<String, Long> typeMap = vehicleTypeService.optionsBySelectOrg(dto.getOrganizationId()).stream().collect(Collectors.toMap(OptionVM::getName, OptionVM::getId, (k1, k2) -> k2));
            Long vehicleType = typeMap.get(dto.getVehicleType());
            if (null == vehicleType) {
                reader.addCellError(dto, VehicleImportDto.IDX_VEHICLE_TYPE, "车辆类型不存在");
            } else {
                Map<String, Long> modelMap = vehicleModelService.vehicleModelOptionsVehicleType(vehicleType).stream().collect(Collectors.toMap(OptionVM::getName, OptionVM::getId, (k1, k2) -> k2));
                Long vehicleModel = modelMap.get(dto.getVehicleModel());
                dto.setVehicleTypeId(vehicleType);
                dto.setVehicleModelId(vehicleModel);
                if (null == vehicleModel) {
                    reader.addCellError(dto, VehicleImportDto.IDX_VEHICLE_MODEL, "车辆型号不存在");
                }
            }
        });
    }

    public void updateVehicle(List<VehicleImportDto> dtoList) {
        dtoList.forEach(dto -> {
            Vehicle vehicle = vehicleDao.findVehicleByTerminalCode(dto.getTerminalCode());
            RestResult unregister = unregister(dto.getTerminalCode());
            if (!unregister.getIsSuccess()) {
                throw new BadRequestException(unregister.getErrorCode() + "");
            }
            RestResult register = register(dto.getVin(), vehicleDao.findProtocolTypeAndApiKey(dto.getTerminalCode()));
            if (!register.getIsSuccess()) {
                throw new BadRequestException(register.getErrorCode() + "");
            }
            redisQueryService.delete(dto.getTerminalCode());
            vehicle.setVin(dto.getVin());
            vehicle.setOrganizationId(dto.getOrganizationId());
            vehicle.setServiceStatus(ServiceStatus.HAS_OPENED);
            vehicle.setServiceStartDate(Integer.parseInt(LocalDateTimeUtils.getCurrentDay(LocalDateTimeUtils.DATE_PATTERN_A)));
            vehicle.setServicePeriod(Integer.parseInt(dto.getPeriod()));
            String endDate = LocalDateTimeUtils.addMonth(Integer.parseInt(dto.getPeriod()), LocalDateTimeUtils.DATE_PATTERN_A, String.valueOf(vehicle.getServiceStartDate()));
            vehicle.setServiceEndDate(Integer.parseInt(endDate));
            String currentDay = LocalDateTimeUtils.getCurrentDay(LocalDateTimeUtils.DATE_PATTERN_A);
            vehicle.setRegistDate(Integer.parseInt(currentDay));
            vehicle.setVehicleTypeId(dto.getVehicleTypeId());
            vehicle.setVehicleModelId(dto.getVehicleModelId());
            vehicle.setUpdateTime(System.currentTimeMillis());
            vehicle.setUpdateUserAccount(BaseContextHandler.getLoginName());
            vehicle.setUpdateUserRealname(BaseContextHandler.getName());
            vehicleDao.updateById(vehicle);
            VehicleRealtime realtime = vehicleRealtimeDao.createLambdaQuery()
                    .andEq(VehicleRealtime::getVin, dto.getTerminalCode())
                    .single();
            realtime.setVin(dto.getVin());
            vehicleRealtimeDao.updateById(realtime);
        });
    }

    public VehicleBaseInfoVM baseInfoByVin(String vin) {
        VehicleBaseInfoVM vm = vehicleDao.baseInfoByVin(vin);
        if (Objects.isNull(vm)) {
            return new VehicleBaseInfoVM();
        }
        if (vehicleDao.tlaCount(vm.getRootOrgId()) > 0) {
            vm.setShowTla(true);
        } else {
            vm.setShowTla(false);
        }
        return vm;
    }

    public Map<String, Object> monitorVehicleInfo(String code) {
        return Optional.ofNullable(vehicleDao.findVehicleByCode(code))
                .map(vehicle -> {
                    HashMap<String, Object> result = new HashMap<>(2);
                    result.put("BASE", vehicleDao.monitorVehicleInfoByVin(vehicle.getVin()));
                    result.put("REALTIME", monitorVehicleRealtimeInfo(vehicle.getVin()));
                    return result;
                })
                .orElseThrow(() -> new BadRequestException(ErrorConstants.VEHICLE_NOT_EXIST));
    }

    public FrameVehicleVM frameVehicleInfo(String vin) {
        return Optional.ofNullable(vehicleDao.frameVehicleInfo(vin))
                .map(vm -> {
                    Long totalWorkTime = vm.getTotalWorkTime();
                    if (Objects.nonNull(totalWorkTime)) {
                        BigDecimal divide = BigDecimal.valueOf(totalWorkTime).divide(BigDecimal.valueOf(3600000), BigDecimal.ROUND_HALF_UP, 2);
                        vm.setWorkTime(divide);
                    }
                    return vm;
                })
                .orElseGet(FrameVehicleVM::new);
    }

    public VehicleRealtimeVM monitorVehicleRealtimeInfo(String vin) {
        return Optional.ofNullable(vehicleRealtimeDao.createLambdaQuery().andEq(VehicleRealtime::getVin, vin).single())
                .map(entity -> {
                    VehicleRealtimeVM realtimeVM = realtimeMapper.toVM(entity);
                    List<VehicleLockItemVM> lockItemVMS = vehicleDao.vehicleLockInfoByVin(vin);
                    if (!lockItemVMS.isEmpty()) {
                        Map<String, VehicleLockItemVM> itemMap = lockItemVMS.stream().collect(Collectors.toMap(VehicleLockItemVM::getItemCode, v -> v));
                        setLockStatus(realtimeVM, itemMap);
                    }
                    return realtimeVM;
                })
                .orElseGet(VehicleRealtimeVM::new);

    }

    public ArrayList<Map> trackData(String vin, TrackDataQuery query) {
        String terminalType = apiServiceImp.getTerminalType(vin);
        ArrayList<Map> records = Lists.newArrayList();
        hbaseService.queryTrackFilterData(terminalType, vin, query.getBeginTime(), query.getEndTime(),
                record -> record.getCmdID() == 127,
                record -> JSON.parseObject(record.getBody(), Map.class), records::add);
        return records;
    }


    /**
     * 每个月第一天凌晨扫描 将服务期到期日期 范围在上个月 的车辆 设为已过期
     */
    public void vehicleServiceStatusJob() {
        Instant instant = LocalDateTime.now().toInstant(ZoneOffset.of("+8"));
        LocalDateTime yesterday = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                .plusDays(-1).withHour(23).withMinute(59).withSecond(59).withNano(0);
        String end = LocalDateTimeUtils.formatDateTime(yesterday, "yyyyMMdd");
        LocalDateTime localDateTime = yesterday.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        String begin = LocalDateTimeUtils.formatDateTime(localDateTime, "yyyyMMdd");
        List<Vehicle> vehicleList = vehicleDao.createLambdaQuery().andGreatEq(Vehicle::getServiceEndDate, begin)
                .andLessEq(Vehicle::getServiceEndDate, end)
                .select();
        if (vehicleList.isEmpty()) {
            return;
        }
        vehicleList.forEach(vehicle -> {
            vehicle.setServiceStatus(ServiceStatus.EXPIRE);
            vehicleDao.updateById(vehicle);
        });
    }

    /**
     * 获取车辆实时信息
     *
     * @param code
     * @return
     */
    public VehicleRealtimeVM monitorVehicleRealTimeInfo(String code) {
        return Optional.ofNullable(vehicleDao.findVehicleByCode(code))
                .map(vehicle -> {
                    return monitorVehicleRealtimeInfo(vehicle.getVin());
                })
                .orElseThrow(() -> new BadRequestException(ErrorConstants.VEHICLE_NOT_EXIST));
    }

    /**
     * 获取车辆基本信息
     *
     * @param code
     * @return
     */
    public VehicleVM monitorVehicleBaseInfo(String code) {
        return Optional.ofNullable(vehicleDao.findVehicleByCode(code))
                .map(vehicle -> {
                    return vehicleDao.monitorVehicleInfoByVin(vehicle.getVin());
                })
                .orElseThrow(() -> new BadRequestException(ErrorConstants.VEHICLE_NOT_EXIST));
    }


    public void trackDataExport(String vin, TrackDataQuery query,
                                HttpServletRequest request, HttpServletResponse response) {
        int[] permissions = {109611};
        String[] titles = {"时间", "所在位置", "车速(km/h)", "方向", "定位状态"};
        String[] columns = {"WORK_TIME", "LOCATION", "WORK_VEHICLE_SPEED", "DIRE", "STA_LOC"};
        boolean b = accountApiClient.checkPermission(permissions);
        ArrayList<Map> list = this.trackData(vin, query);
        if (!b) {
            ArrayList<String> titleList = new ArrayList<>(Arrays.asList(titles));
            titleList.remove(1);
            titles = titleList.toArray(new String[titleList.size()]);
            ArrayList<String> columnsList = new ArrayList<>(Arrays.asList(columns));
            columnsList.remove(1);
            columns = columnsList.toArray(new String[columnsList.size()]);
        }
        StringBuilder builder = new StringBuilder();
        List<HashMap<String, Object>> collect = list.stream().map(workData -> {
            HashMap<String, Object> map = Maps.newHashMap();
            Map<String, Object> condition = (Map) workData.get(CONDITION_MSG);
            map.put(WORK_TIME, LocalDateTimeUtils.formatDay((Long) condition.get(WORK_TIME), LocalDateTimeUtils.DATE_TIME_PATTERN));
            map.put(WORK_VEHICLE_SPEED, condition.get(WORK_VEHICLE_SPEED));
            Map<String, Object> o = (Map) condition.get(WORK_VEHICLE_LOC_DATA);
            if (b) {
                String location = builder.append(o.get(LOC_DATA_PROVINCE)).append(o.get(LOC_DATA_CITY)).append(o.get(LOC_DATA_DISTRICT)).toString();
                builder.delete(0, location.length());
                map.put(LOCATION, location);
            }
            Map<String, Integer> status = (Map) condition.get(STATUS_INFO);
            map.put(STA_LOC, 0 == status.get(STA_LOC) ? "未定位" : "已定位");
            Integer dire = (Integer) condition.get(WORK_VEHICLE_DIRE);
            map.put(DIRE, dire(dire));
            return map;
        }).collect(Collectors.toList());

        try {
            ExcelWriter.exportExcel(request, response, "轨迹回放", titles, columns, collect);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }

    private String dire(Integer dire) {
        if (Objects.isNull(dire)) {
            return null;
        }
        if (dire == 0) {
            return "正北";
        }
        if (0 < dire && dire > 90) {
            return "东北";
        }
        if (dire == 90) {
            return "正东";
        }
        if (dire > 90 && dire < 180) {
            return "东南";
        }
        if (dire == 180) {
            return "正南";
        }
        if (dire > 180 && dire < 270) {
            return "西南";
        }
        if (dire == 270) {
            return "正西";
        }
        if (dire > 270 && dire < 360) {
            return "西北";
        }
        return null;
    }
}
