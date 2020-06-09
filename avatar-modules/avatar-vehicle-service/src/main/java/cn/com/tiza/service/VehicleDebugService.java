package cn.com.tiza.service;


import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dao.VehicleDao;
import cn.com.tiza.domain.CmdDebug;
import cn.com.tiza.domain.VehicleDebugLog;
import cn.com.tiza.service.dto.LockEnum;
import cn.com.tiza.service.mapper.CmdDebugMapper;
import cn.com.tiza.web.rest.vm.CmdDebugVM;
import cn.com.tiza.web.rest.vm.VehicleLockItemVM;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.tiza.domain.VehicleDebug;
import cn.com.tiza.service.dto.VehicleDebugDto;
import cn.com.tiza.service.dto.VehicleDebugQuery;
import cn.com.tiza.service.mapper.VehicleDebugMapper;
import cn.com.tiza.web.rest.vm.VehicleDebugVM;
import cn.com.tiza.dao.VehicleDebugDao;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service
 * gen by beetlsql 2020-03-17
 *
 * @author tiza
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class VehicleDebugService {

    @Autowired
    private VehicleDebugDao vehicleDebugDao;

    @Autowired
    private VehicleDebugMapper vehicleDebugMapper;

    @Autowired
    private VehicleDao vehicleDao;

    @Autowired
    private CmdDebugMapper cmdDebugMapper;

    public PageQuery<VehicleDebugVM> findAll(VehicleDebugQuery query) {
        if (Objects.isNull(query.getOrganizationId())) {
            query.setOrganizationId(BaseContextHandler.getOrgId());
        }
        query.setFinanceId(BaseContextHandler.getFinanceId());
        PageQuery<VehicleDebugVM> pageQuery = query.toPageQuery();
        vehicleDebugDao.pageQuery(pageQuery);
        List<VehicleDebugVM> vehicleDebugVMList = pageQuery.getList().stream().map(this::setTerminalStatus).collect(Collectors.toList());
        pageQuery.setList(vehicleDebugVMList);
        List<VehicleDebugVM> list = pageQuery.getList();
        if (list.isEmpty()) {
            return pageQuery;
        }
        List<String> vinList = list.stream().map(VehicleDebugVM::getVin).collect(Collectors.toList());
        query.setVinList(vinList);
        List<VehicleLockItemVM> vehicleLockList = vehicleDebugDao.vehicleLockList(query);
        if (vehicleLockList.isEmpty()) {
            return pageQuery;
        }
        setLockStatus(pageQuery.getList(), vehicleLockList);
        return pageQuery;
    }

    public PageQuery<VehicleDebugVM> getVehicleList(VehicleDebugQuery query) {
        if (Objects.isNull(query.getOrganizationId())) {
            query.setOrganizationId(BaseContextHandler.getOrgId());
        }
        List<String> vinList = vehicleDebugDao.getVinListInDebug(query.getOrganizationId());
        if (vinList.isEmpty()) {
            vinList = null;
        }
        query.setVinList(vinList);
        PageQuery pageQuery = query.toPageQuery();
        vehicleDebugDao.getVehicleList(pageQuery);
        return pageQuery;
    }

    public PageQuery<VehicleDebugVM> getVehicleDebugLogList(VehicleDebugQuery query) {
        if (Objects.isNull(query.getOrganizationId())) {
            query.setOrganizationId(BaseContextHandler.getOrgId());
        }
        query.setFinanceId(BaseContextHandler.getFinanceId());
        PageQuery<VehicleDebugVM> pageQuery = query.toPageQuery();
        vehicleDebugDao.getVehicleDebugLogList(pageQuery);
        List<VehicleDebugVM> vehicleDebugVMList = pageQuery.getList().stream().map(this::setTerminalStatus).collect(Collectors.toList());
        pageQuery.setList(vehicleDebugVMList);
        List<VehicleDebugVM> list = pageQuery.getList();
        if (list.isEmpty()) {
            return pageQuery;
        }
        List<String> vinList = list.stream().map(VehicleDebugVM::getVin).collect(Collectors.toList());
        query.setVinList(vinList);
        List<VehicleLockItemVM> vehicleLockList = vehicleDebugDao.vehicleLockList(query);
        if (vehicleLockList.isEmpty()) {
            return pageQuery;
        }
        setLockStatus(pageQuery.getList(), vehicleLockList);
        return pageQuery;
    }

    public Optional<VehicleDebug> get(Long id) {
        return Optional.ofNullable(vehicleDebugDao.single(id));
    }

    public Optional<VehicleDebugVM> getVehicleDebugById(Long id) {
        VehicleDebugVM vehicleDebugVM = vehicleDebugDao.getVehicleDebugById(id);
        List<VehicleDebugLog> logList = vehicleDebugDao.getDebugLogList(vehicleDebugVM.getVin());
        if (!logList.isEmpty()) {
            vehicleDebugVM.setCmdList(logList);
        } else {
            CmdDebug cmdDebug = vehicleDebugDao.getCmdDebugByInfo(vehicleDebugVM.getOrganizationId(), vehicleDebugVM.getVehicleModelId());
            CmdDebugVM cmdDebugVM = cmdDebugMapper.toVM(cmdDebug);
            for (HashMap<String, String> map : cmdDebugVM.getCmdList()) {
                VehicleDebugLog vehicleDebugLog = new VehicleDebugLog();
                vehicleDebugLog.setItemKey(map.get("itemCode"));
                vehicleDebugLog.setItemName(map.get("itemName"));
                logList.add(vehicleDebugLog);
            }
            vehicleDebugVM.setCmdList(logList);
        }
        return Optional.ofNullable(vehicleDebugVM);
    }

    public Optional<HashMap> getCountByStatus() {
        List<VehicleDebugVM> list = vehicleDebugDao.getCountByStatus();
        HashMap map = new HashMap(8);
        for (VehicleDebugVM temp : list) {
            if (temp.getStatus() == 0) {
                map.put("waitCount", temp.getCount());
            } else if (temp.getStatus() == 1) {
                map.put("successCount", temp.getCount());
            } else if (temp.getStatus() == 2) {
                map.put("failCount", temp.getCount());
            }
        }
        return Optional.ofNullable(map);
    }

    public void create(String[] vins) {
        List<VehicleDebug> vehicleDebugList = new ArrayList<>();
        List<String> vinList = vehicleDebugDao.getVinListInDebug(BaseContextHandler.getOrgId());
        for (String vin : vins) {
            if (vinList.contains(vin)) {
                continue;
            }
            VehicleDebug vehicleDebug = new VehicleDebug();
            vehicleDebug.setVin(vin);
            vehicleDebug.setCreateTime(System.currentTimeMillis());
            vehicleDebug.setCreateUserAccount(BaseContextHandler.getLoginName());
            vehicleDebug.setCreateUserRealname(BaseContextHandler.getName());
            vehicleDebug.setTestStatus(1);
            vehicleDebug.setStatus(0);
            vehicleDebug.setDebugBeginTime(System.currentTimeMillis());
            vehicleDebug.setDebugEndTime(getNextMonthLast());
            vehicleDebugList.add(vehicleDebug);
        }
        vehicleDebugDao.insertBatch(vehicleDebugList);
    }

    public Optional<VehicleDebug> update(Long id, VehicleDebugDto command) {
        return get(id).map(entity -> {
            entity.setStatus(command.getStatus());
            entity.setDebugUserId(BaseContextHandler.getUserID());
            entity.setUpdateTime(System.currentTimeMillis());
            entity.setUpdateUserAccount(BaseContextHandler.getLoginName());
            entity.setUpdateUserRealname(BaseContextHandler.getName());
            vehicleDebugDao.updateById(entity);
            return entity;
        });
    }

    public void delete(Long id) {
        vehicleDebugDao.deleteById(id);
    }

    public void deleteByVehicleId(Long vehicleId) {
        String id = vehicleDebugDao.getDebugIdByVehicleId(vehicleId);
        if (!Objects.isNull(id)){
            delete(Long.parseLong(id));
        }
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            delete(id);
        }
    }

    public void deleteByVehicleId(Long[] ids) {
        for (Long id : ids) {
            deleteByVehicleId(id);
        }
    }

    private Long getNextMonthLast() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        //将分钟至59
        cal.set(Calendar.MINUTE, 59);
        //将秒至59
        cal.set(Calendar.SECOND, 59);
        //将毫秒至999
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTimeInMillis();
    }

    /**
     * @param debugVMList     车辆列表
     * @param vehicleLockList 车辆锁车配置列表
     */
    private void setLockStatus(List<VehicleDebugVM> debugVMList, List<VehicleLockItemVM> vehicleLockList) {
        Map<String, List<VehicleLockItemVM>> lockGroup = vehicleLockList
                .stream()
                .collect(Collectors.groupingBy(VehicleLockItemVM::getVin));
        debugVMList.forEach(debugVM -> {
            List<VehicleLockItemVM> vehicleLockItems = lockGroup.get(debugVM.getVin());
            if (Objects.isNull(vehicleLockItems)) {
                return;
            }
            Map<String, VehicleLockItemVM> itemMap = vehicleLockItems.stream().collect(Collectors.toMap(VehicleLockItemVM::getItemCode, v -> v, (oldValue, newValue) -> newValue));
            HashMap<String, Integer> lockStatus = Maps.newHashMap();
            debugVM.setLockStatus(lockStatus);
            if (Objects.nonNull(itemMap.get(LockEnum.CONTROLLER_LOCK.name()))) {
                lockStatus.put(LockEnum.CONTROLLER_LOCK.name(), null == debugVM.getLock() ? 0 : debugVM.getLock());
            }
            if (Objects.nonNull(itemMap.get(LockEnum.RELAY_LOCK_1.name()))) {
                lockStatus.put(LockEnum.RELAY_LOCK_1.name(), null == debugVM.getOneLevelLock() ? 0 : debugVM.getOneLevelLock());
            }
            if (Objects.nonNull(itemMap.get(LockEnum.RELAY_LOCK_2.name()))) {
                lockStatus.put(LockEnum.RELAY_LOCK_2.name(), null == debugVM.getTwoLevelLock() ? 0 : debugVM.getTwoLevelLock());
            }
            if (Objects.nonNull(itemMap.get(LockEnum.RELAY_LOCK_3.name()))) {
                lockStatus.put(LockEnum.RELAY_LOCK_3.name(), null == debugVM.getThreeLevelLock() ? 0 : debugVM.getThreeLevelLock());
            }
            debugVM.setLockStatus(lockStatus);
        });
    }

    private VehicleDebugVM setTerminalStatus(VehicleDebugVM vehicleDebugVM) {
        if (null != vehicleDebugVM.getDataUpdateTime() && vehicleDebugVM.getDataUpdateTime() + 300000 > System.currentTimeMillis()) {
            vehicleDebugVM.setTerminalStatus(1);
        } else {
            vehicleDebugVM.setTerminalStatus(0);
        }
        return vehicleDebugVM;
    }

    /**
     * 每个月第一天凌晨扫描 将服务期到期日期 范围在上个月 的车辆 设为已过期
     */
    public void vehicleTestServiceJob() {
        List<VehicleDebug> vehicleDebugList = vehicleDebugDao.createLambdaQuery().andEq(VehicleDebug::getTestStatus, 1)
                .andLess(VehicleDebug::getDebugEndTime, System.currentTimeMillis())
                .select();
        vehicleDebugList.forEach(vehicleDebug -> {
                    vehicleDebug.setTestStatus(0);
                    vehicleDebugDao.updateById(vehicleDebug);
                }
        );

    }
}
