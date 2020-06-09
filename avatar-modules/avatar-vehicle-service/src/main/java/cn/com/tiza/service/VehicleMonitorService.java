package cn.com.tiza.service;

import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dao.VehicleDao;
import cn.com.tiza.dao.VehicleMonitorDao;
import cn.com.tiza.domain.Vehicle;
import cn.com.tiza.dto.RestDataRecord;
import cn.com.tiza.excel.ExcelWriter;
import cn.com.tiza.rest.ExcelController;
import cn.com.tiza.service.dto.LockEnum;
import cn.com.tiza.service.dto.TrackDataQuery;
import cn.com.tiza.service.dto.VehicleMonitorQuery;
import cn.com.tiza.service.dto.WorkConditionDto;
import cn.com.tiza.util.LocalDateTimeUtils;
import cn.com.tiza.web.rest.AccountApiClient;
import cn.com.tiza.web.rest.FenceVehicleClient;
import cn.com.tiza.web.rest.vm.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.googlecode.aviator.AviatorEvaluator;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tiza
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class VehicleMonitorService extends ExcelController {
    @Autowired
    private VehicleMonitorDao vehicleMonitorDao;
    @Autowired
    private GrampusDataQueryService grampusDataQueryService;
    @Autowired
    private RedisQueryService redisQueryService;
    @Autowired
    private FenceVehicleClient fenceVehicleClient;
    @Autowired
    private DashBoardService dashBoardService;
    @Autowired
    private GrampusApiServiceImp apiServiceImp;
    @Autowired
    private VehicleDao vehicleDao;
    @Autowired
    private AccountApiClient accountApiClient;

    private static int[] permission = new int[]{109611};

    public PageQuery<VehicleMonitorVM> findAll(VehicleMonitorQuery query) {
        if (Objects.isNull(query.getOrganizationId())) {
            query.setOrganizationId(BaseContextHandler.getOrgId());
        }
        query.setFinanceId(BaseContextHandler.getFinanceId());
        if (!Objects.isNull(query.getStatus())) {
            if (query.getStatus() == 1) {
                query.setOnline(1);
                query.setDataUpdateTime(System.currentTimeMillis() - 5 * 60 * 1000);
                query.setDays(null);
            } else if (query.getStatus() == 2) {
                query.setFaultStatus(1);
                query.setDays(null);
            } else if (query.getStatus() == 3) {
                query.setOnline(1);
                query.setFaultStatus(1);
                query.setDataUpdateTime(System.currentTimeMillis() - 5 * 60 * 1000);
                query.setDays(null);
            } else if (query.getStatus() == 4) {
                query.setLock(1);
                query.setDays(null);
            } else if (query.getStatus() == 5) {
                if (!Objects.isNull(query.getDays())) {
                    Long days = query.getDays();
                    Long time = System.currentTimeMillis() - days * 24 * 60 * 60 * 1000;
                    query.setDays(time);
                } else {
                    query.setDays(System.currentTimeMillis() - 5 * 60 * 1000);
                }
            }
        }else {
            query.setDays(null);
        }
        PageQuery<VehicleMonitorVM> pageQuery = query.toPageQuery();
        vehicleMonitorDao.getVehicleMonitorList(pageQuery);
        List<VehicleMonitorVM> vehicleMonitorVMList = pageQuery.getList().stream().map(this::setTerminalStatus).collect(Collectors.toList());
        pageQuery.setList(vehicleMonitorVMList);
        List<VehicleMonitorVM> list = pageQuery.getList();
        if (list.isEmpty()) {
            return pageQuery;
        }
        List<String> vinList = list.stream().map(VehicleMonitorVM::getVin).collect(Collectors.toList());
        query.setVinList(vinList);
        List<VehicleLockItemVM> vehicleLockList = vehicleMonitorDao.getVehicleLockList(query);
        if (vehicleLockList.isEmpty()) {
            return pageQuery;
        }
        setLockStatus(pageQuery.getList(), vehicleLockList);
        return pageQuery;
    }


    public PageQuery<VehicleMonitorVM> findByCon(VehicleMonitorQuery query) {
        if (Objects.isNull(query.getOrganizationId())) {
            query.setOrganizationId(BaseContextHandler.getOrgId());
        }
        query.setFinanceId(BaseContextHandler.getFinanceId());
        //在线状态
        if (!Objects.isNull(query.getOnline())) {
            query.setDataUpdateTime(System.currentTimeMillis() - 300000);
        }
        PageQuery<VehicleMonitorVM> pageQuery = query.toPageQuery();
        vehicleMonitorDao.getVehicleMonitorOtherList(pageQuery);
        List<VehicleMonitorVM> vehicleMonitorVMList = pageQuery.getList().stream().map(vehicle -> {
            //在线状态
            if (null != vehicle.getDataUpdateTime() && vehicle.getDataUpdateTime() + 300000 > System.currentTimeMillis()) {
                vehicle.setOnline(1);
            } else {
                vehicle.setOnline(0);
            }
            //锁车状态
            if (Objects.nonNull(vehicle.getLock()) && vehicle.getLock() == 1) {
                vehicle.setLock(1);
            } else {
                if (Objects.nonNull(vehicle.getOneLevelLock()) && vehicle.getOneLevelLock() == 1) {
                    vehicle.setLock(1);
                } else {
                    if (Objects.nonNull(vehicle.getTwoLevelLock()) && vehicle.getTwoLevelLock() == 1) {
                        vehicle.setLock(1);
                    } else {
                        if (Objects.nonNull(vehicle.getThreeLevelLock()) && vehicle.getThreeLevelLock() == 1) {
                            vehicle.setLock(1);
                        } else {
                            vehicle.setLock(0);
                        }
                    }
                }
            }
            return vehicle;
        }).collect(Collectors.toList());
        pageQuery.setList(vehicleMonitorVMList);
        List<VehicleMonitorVM> list = pageQuery.getList();
        if (list.isEmpty()) {
            return pageQuery;
        }
        List<String> vinList = list.stream().map(VehicleMonitorVM::getVin).collect(Collectors.toList());
        query.setVinList(vinList);
        List<VehicleLockItemVM> vehicleLockList = vehicleMonitorDao.getVehicleLockList(query);
        if (vehicleLockList.isEmpty()) {
            return pageQuery;
        }
        setLockStatus(pageQuery.getList(), vehicleLockList);
        return pageQuery;
    }

    public List<FunctionSetItemVM> getItemListByVin(String vin) {
        return vehicleMonitorDao.getItemListByVin(vin);
    }

    public HistoryDataVM getTrackDataList(String vin, TrackDataQuery query) {
        String terminalType = apiServiceImp.getTerminalType(vin);
        ArrayList<Map<String, Object>> records = Lists.newArrayList();
        grampusDataQueryService.queryTrackFilterData(terminalType, vin, query.getBeginTime(), query.getEndTime(), 127, false, null, record -> JSON.parseObject(record.getBody(), Map.class), record -> records.add(record));
        //获取这辆车配置的功能集
        List<FunctionSetItemVM> itemVMList = getItemListByVin(vin);
        Map<String, List<FunctionSetItemVM>> itemGroupMap = itemVMList.stream().collect(Collectors.groupingBy(FunctionSetItemVM::getEnName));
        List<WorkConditionVM> list = new ArrayList<>();
        List<Map<String, Object>> recordList;
        if (query.getPage() * query.getPageSize() - 1 > records.size()) {
            recordList = records.subList((query.getPage() - 1) * query.getPageSize(), records.size());
        } else {
            recordList = records.subList((query.getPage() - 1) * query.getPageSize(), (query.getPage()) * query.getPageSize() - 1);
        }
        for (Map<String, Object> record : recordList) {
            WorkConditionVM workConditionVM = new WorkConditionVM();
            List<FunctionSetItemVM> functionList = getHistoryBaseInfo(vin, record);
            //获取工况数据并转换为Map
            Map<String, Object> objectMap = JSONObject.parseObject(record.get("CONDITION_MSG").toString(), Map.class);
            Map<String, String> paramMap = new HashMap<>();
            //获取配置项信息并转化为Map
            List<WorkConditionDto> conditionList = JSONArray.parseArray(objectMap.get("WORK_VEHICLE_PARAMETERS").toString(), WorkConditionDto.class);
            if (!Objects.isNull(conditionList)) {
                paramMap = conditionList.stream().filter(workConditionDto -> !Objects.isNull(workConditionDto.getNAME())).collect(Collectors.toMap(e -> e.getNAME(), e -> e.getVALUE(), (k1, k2) -> k2));
            }
            if (Objects.isNull(paramMap)) {
                functionList.addAll(itemVMList);
                workConditionVM.setItemVMList(functionList);
                continue;
            }
            functionList = handle(itemGroupMap, paramMap, functionList);
            workConditionVM.setItemVMList(functionList);
            list.add(workConditionVM);
        }
        HistoryDataVM historyDataVM=new HistoryDataVM();
        historyDataVM.setConditionVMList(list);
        historyDataVM.setPageCount(records.size());
        return historyDataVM;
    }

    public WorkConditionVM getTrackData(String vin) {
        Boolean flag = true;
        List<FunctionSetItemVM> itemVMList = getItemListByVin(vin);
        if (itemVMList.size() > 0) {
            flag = false;
        }
        Map<String, List<FunctionSetItemVM>> itemGroupMap = itemVMList.stream().collect(Collectors.groupingBy(FunctionSetItemVM::getEnName));
        WorkConditionVM workConditionVM = new WorkConditionVM();
        List<FunctionSetItemVM> itemsVM = getBaseInfo(vin, flag);
        Map<String, String> objectMap = redisQueryService.ciData(vin);
        if (Objects.isNull(objectMap)) {
            itemsVM.addAll(itemVMList);
            workConditionVM.setItemVMList(itemsVM);
            return workConditionVM;
        }
        itemsVM = handle(itemGroupMap, objectMap, itemsVM);
        workConditionVM.setItemVMList(itemsVM);
        return workConditionVM;
    }

    private VehicleMonitorVM setTerminalStatus(VehicleMonitorVM vehicleMonitorVM) {
        if (null != vehicleMonitorVM.getDataUpdateTime() && vehicleMonitorVM.getDataUpdateTime() + 300000 > System.currentTimeMillis()) {
            vehicleMonitorVM.setTerminalStatus(1);
        } else {
            vehicleMonitorVM.setTerminalStatus(0);
        }
        return vehicleMonitorVM;
    }

    /**
     * @param monitorVMList   车辆列表
     * @param vehicleLockList 车辆锁车配置列表
     */
    private void setLockStatus(List<VehicleMonitorVM> monitorVMList, List<VehicleLockItemVM> vehicleLockList) {
        Map<String, List<VehicleLockItemVM>> lockGroup = vehicleLockList
                .stream()
                .collect(Collectors.groupingBy(VehicleLockItemVM::getVin));
        monitorVMList.forEach(monitorVM -> {
            List<VehicleLockItemVM> vehicleLockItems = lockGroup.get(monitorVM.getVin());
            if (Objects.isNull(vehicleLockItems)) {
                return;
            }
            Map<String, VehicleLockItemVM> itemMap = vehicleLockItems.stream().collect(Collectors.toMap(VehicleLockItemVM::getItemCode, v -> v, (oldValue, newValue) -> newValue));
            HashMap<String, Integer> lockStatus = Maps.newHashMap();
            monitorVM.setLockStatus(lockStatus);
            if (Objects.nonNull(itemMap.get(LockEnum.CONTROLLER_LOCK.name()))) {
                lockStatus.put(LockEnum.CONTROLLER_LOCK.name(), null == monitorVM.getLock() ? 0 : monitorVM.getLock());
            }
            if (Objects.nonNull(itemMap.get(LockEnum.RELAY_LOCK_1.name()))) {
                lockStatus.put(LockEnum.RELAY_LOCK_1.name(), null == monitorVM.getOneLevelLock() ? 0 : monitorVM.getOneLevelLock());
            }
            if (Objects.nonNull(itemMap.get(LockEnum.RELAY_LOCK_2.name()))) {
                lockStatus.put(LockEnum.RELAY_LOCK_2.name(), null == monitorVM.getTwoLevelLock() ? 0 : monitorVM.getTwoLevelLock());
            }
            if (Objects.nonNull(itemMap.get(LockEnum.RELAY_LOCK_3.name()))) {
                lockStatus.put(LockEnum.RELAY_LOCK_3.name(), null == monitorVM.getThreeLevelLock() ? 0 : monitorVM.getThreeLevelLock());
            }
            monitorVM.setLockStatus(lockStatus);
        });
    }

    private List<FunctionSetItemVM> getBaseInfo(String vin, Boolean flag) {
        List<FunctionSetItemVM> list = new ArrayList<>();
        VehicleMonitorVM vehicleMonitorVM = dashBoardService.getVehicleInfoByVin(vin);
        FunctionSetItemVM functionSetItemVM = new FunctionSetItemVM();
        functionSetItemVM.setEnName("VIN");
        functionSetItemVM.setName("机器序列号");
        functionSetItemVM.setValue(vehicleMonitorVM.getVin());
        list.add(functionSetItemVM);
        if (flag) {
            FunctionSetItemVM functionSetItemVM2 = new FunctionSetItemVM();
            functionSetItemVM2.setEnName("ACC");
            functionSetItemVM2.setName("ACC时间");
            if (!Objects.isNull(vehicleMonitorVM.getTotalWorkTime())) {
                functionSetItemVM2.setValue(String.valueOf(vehicleMonitorVM.getTotalWorkTime()));
            }
            list.add(functionSetItemVM2);
        }
        FunctionSetItemVM functionSetItemVM3 = new FunctionSetItemVM();
        functionSetItemVM3.setEnName("UPDATETIME");
        functionSetItemVM3.setName("工况时间");
        functionSetItemVM3.setValue(vehicleMonitorVM.getUpdateTime());
        list.add(functionSetItemVM3);
        if (accountApiClient.checkPermission(permission)) {
            FunctionSetItemVM functionSetItemVM4 = new FunctionSetItemVM();
            functionSetItemVM4.setEnName("ADDRESS");
            functionSetItemVM4.setName("位置");
            if (!Objects.isNull(vehicleMonitorVM.getLastLon()) && !Objects.isNull(vehicleMonitorVM.getLastLat())) {
                String address = fenceVehicleClient.getLocation(vehicleMonitorVM.getLastLon().toString(), vehicleMonitorVM.getLastLat().toString());
                functionSetItemVM4.setValue(address);
            }
            list.add(functionSetItemVM4);
        }
        return list;
    }

    private String handleResult(String expression) {
        String result="";
        try {
            result = String.format("%.2f", (Double) AviatorEvaluator.execute(expression));
        } catch (Exception e) {
            result = expression;
        }finally {
            return result;
        }
    }

    private String handleExpression(String expression, String result) {
        HashMap<String, String> hashMap = JSON.parseObject(expression, HashMap.class);
        Map map = new HashMap();
        map.put("val", Double.valueOf(result));
        for (String key : hashMap.keySet()) {
            if ((Boolean) AviatorEvaluator.execute(key, map)) {
                return hashMap.get(key);
            }
        }
        return result;
    }

    private List<FunctionSetItemVM> getHistoryBaseInfo(String vin, Map<String, Object> record) {
        List<FunctionSetItemVM> list = new ArrayList<>();
        FunctionSetItemVM functionSetItemVM2 = new FunctionSetItemVM();
        functionSetItemVM2.setEnName("UPDATETIME");
        functionSetItemVM2.setName("工况时间");
        long time = Long.parseLong(record.get("TIME").toString());
        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(time / 1000, 0, ZoneOffset.ofHours(8));
        functionSetItemVM2.setValue(LocalDateTimeUtils.formatDateTime(localDateTime, null));
        list.add(functionSetItemVM2);
        FunctionSetItemVM functionSetItemVM = new FunctionSetItemVM();
        functionSetItemVM.setEnName("RECEIVETIME");
        functionSetItemVM.setName("接收时间");
        long receiveTime = Long.parseLong(record.get("REC_TIME").toString());
        LocalDateTime recTime = LocalDateTime.ofEpochSecond(receiveTime / 1000, 0, ZoneOffset.ofHours(8));
        functionSetItemVM.setValue(LocalDateTimeUtils.formatDateTime(recTime, null));
        list.add(functionSetItemVM);
        FunctionSetItemVM functionSetItemVM4 = new FunctionSetItemVM();
        functionSetItemVM4.setEnName("LOCSTATUS");
        functionSetItemVM4.setName("定位状态");
        FunctionSetItemVM functionSetItemVM3 = new FunctionSetItemVM();
        functionSetItemVM3.setEnName("ADDRESS");
        functionSetItemVM3.setName("位置");
        //获取位置数据并转换为Map
        Map<String, Object> map = JSONObject.parseObject(record.get("CONDITION_MSG").toString(), Map.class);
        Map<String, Object> objectMap = JSONObject.parseObject(map.get("WORK_VEHICLE_LOC_DATA").toString(), Map.class);
        if (objectMap.get("LOC_DATA_STATUS").equals("1")) {
            functionSetItemVM4.setValue("已定位");
        } else {
            functionSetItemVM4.setValue("未定位");
        }
        list.add(functionSetItemVM4);
        if (Objects.nonNull(objectMap.get("LOC_DATA_PROVINCE")) && Objects.nonNull(objectMap.get("LOC_DATA_CITY")) && Objects.nonNull(objectMap.get("LOC_DATA_DISTRICT"))) {
            functionSetItemVM3.setValue(objectMap.get("LOC_DATA_PROVINCE").toString() + objectMap.get("LOC_DATA_CITY") + objectMap.get("LOC_DATA_DISTRICT"));
        }
        if (accountApiClient.checkPermission(permission)) {
            list.add(functionSetItemVM3);
        }
        return list;
    }

    private List<FunctionSetItemVM> handle(Map<String, List<FunctionSetItemVM>> itemGroupMap, Map<String, String> objectMap, List<FunctionSetItemVM> itemVMList) {
        for (String key : itemGroupMap.keySet()) {
            List<FunctionSetItemVM> list = itemGroupMap.get(key).stream().sorted(Comparator.comparing(FunctionSetItemVM::getSortNum)).collect(Collectors.toList());
            FunctionSetItemVM item = new FunctionSetItemVM();
            item.setEnName(key);
            if (!Objects.isNull(list.get(0).getUnit()) && list.get(0).getUnit().length() > 0) {
                item.setName(list.get(0).getName() + "(" + list.get(0).getUnit() + ")");
            } else {
                item.setName(list.get(0).getName());
            }
            String result = "";
            //是否经过值表达式判断
            boolean flag = false;
            for (FunctionSetItemVM itemVM : list) {
                String res = objectMap.get(itemVM.getCode());
                if (!Objects.isNull(itemVM.getDescription()) && (itemVM.getDescription().length() > 0) && !Objects.isNull(res)) {
                    res = handleExpression(itemVM.getDescription(), res);
                    flag = true;
                }
                if (!Objects.isNull(res)) {
                    if (!Objects.isNull(itemVM.getSeparator())) {
                        result = result + res + itemVM.getSeparator();
                    } else {
                        result += res;
                    }
                }
            }
            if (!flag){
                item.setValue(handleResult(result));
            }else {
                item.setValue(result);
            }
            itemVMList.add(item);
        }
        return itemVMList;
    }

    public void exportHistoryData(String vin, TrackDataQuery query, HttpServletRequest request, HttpServletResponse response) {
//        query.setBeginTime(1589023694211L);
//        query.setEndTime(1589455694211L);
        List<WorkConditionVM> list = getTrackDataList(vin, query).getConditionVMList();
        List<String> titleList = new ArrayList<>();
        List<String> columnList = new ArrayList<>();
        WorkConditionVM workConditionVM = list.get(0);
        TracaData tracaData = new TracaData();
        List<Object> dataList = new ArrayList();
        try {
            HashMap addMap = new HashMap();
            HashMap addValMap = new HashMap();
            for (FunctionSetItemVM functionSetItemVM : workConditionVM.getItemVMList()) {
                titleList.add(functionSetItemVM.getName());
                columnList.add(functionSetItemVM.getEnName());
                addMap.put(functionSetItemVM.getEnName(), Class.forName("java.lang.String"));
            }
            for (WorkConditionVM workCondition : list) {
                for (FunctionSetItemVM functionSetItemVM : workCondition.getItemVMList()) {
                    addValMap.put(functionSetItemVM.getEnName(), functionSetItemVM.getValue());
                }
                Object obj = new ClassUtil().dynamicClass(tracaData, addMap, addValMap);
                dataList.add(obj);
            }
            String[] titles = new String[titleList.size()];
            String[] columns = new String[columnList.size()];
            ExcelWriter.exportExcel(request, response, "历史工况导出", titleList.toArray(titles), columnList.toArray(columns), dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public WorkConditionVM getConditionData(String vin,Long time) {
        String terminalType = apiServiceImp.getTerminalType(vin);
        ArrayList<Map<String, Object>> records = Lists.newArrayList();
        grampusDataQueryService.queryTrackFilterData(terminalType, vin, time-60*1000, time+60*1000, 127, false,
                null, record -> JSON.parseObject(record.getBody(), Map.class), record -> records.add(record));
        Map<String,String> dataMap =getDataMap(records,time);
        return formData(vin,dataMap);
    }

    private Map<String,String>  getDataMap(List<Map<String, Object>> records,Long time){
        Map<String,Object> dataMap=new HashMap<>();
        for (Map<String,Object> map:records){
            if (map.containsValue(time)){
                dataMap=map;
            }
        }
        //获取工况数据并转换为Map
        Map<String, Object> objectMap = JSONObject.parseObject(dataMap.get("CONDITION_MSG").toString(), Map.class);
        Map<String, String> paramMap = new HashMap<>();
        //获取配置项信息并转化为Map
        List<WorkConditionDto> conditionList = JSONArray.parseArray(objectMap.get("WORK_VEHICLE_PARAMETERS").toString(), WorkConditionDto.class);
        if (!Objects.isNull(conditionList)) {
            paramMap = conditionList.stream().filter(workConditionDto -> !Objects.isNull(workConditionDto.getNAME())).collect(Collectors.toMap(e -> e.getNAME(), e -> e.getVALUE(), (k1, k2) -> k2));
        }
        return paramMap;
    }

    private WorkConditionVM formData(String vin,Map map){
        List<FunctionSetItemVM> itemVMList = getItemListByVin(vin);
        Map<String, List<FunctionSetItemVM>> itemGroupMap = itemVMList.stream().collect(Collectors.groupingBy(FunctionSetItemVM::getEnName));
        WorkConditionVM workConditionVM = new WorkConditionVM();
        List<FunctionSetItemVM> itemsVM = getBaseInfo(vin, false);
        if (Objects.isNull(map)) {
            itemsVM.addAll(itemVMList);
            workConditionVM.setItemVMList(itemsVM);
            return workConditionVM;
        }
        itemsVM = handle(itemGroupMap, map, itemsVM);
        workConditionVM.setItemVMList(itemsVM);
        return workConditionVM;
    }
}
