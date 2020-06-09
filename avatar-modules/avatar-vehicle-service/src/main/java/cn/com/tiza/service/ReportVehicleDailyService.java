package cn.com.tiza.service;

import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dao.ReportVehicleDailyDao;
import cn.com.tiza.dao.VehicleMonitorDao;
import cn.com.tiza.domain.ReportVehicleDaily;
import cn.com.tiza.service.dto.ReportVehicleDailyDto;
import cn.com.tiza.service.dto.ReportVehicleDailyQuery;
import cn.com.tiza.service.dto.WorkConditionDto;
import cn.com.tiza.util.LocalDateTimeUtils;
import cn.com.tiza.util.ToolUtil;
import cn.com.tiza.web.rest.errors.BadRequestException;
import cn.com.tiza.web.rest.errors.ErrorConstants;
import cn.com.tiza.web.rest.vm.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReportVehicleDailyService {

    @Autowired
    private ReportVehicleDailyDao dailyDao;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private GrampusDataQueryService grampusDataQueryService;

    @Autowired
    private GrampusApiServiceImp apiServiceImp;

    @Autowired
    private VehicleMonitorDao vehicleMonitorDao;

    public List<ReportVehicleDailyFuelVM> analyseFuelLevel(String vin, ReportVehicleDailyDto dailyDto) {
        List<ReportVehicleDaily> dailies = dailyDao.getData(vin, dailyDto);
        return dailies.stream().map(this::toFuelVM).collect(Collectors.toList());
    }

    private ReportVehicleDailyFuelVM toFuelVM(ReportVehicleDaily daily) {
        ReportVehicleDailyFuelVM vm = new ReportVehicleDailyFuelVM();
        vm.setTime(daily.getDateVal());
        vm.setAvgFuelUse(Optional.ofNullable(daily.getEndLfConsumption()).orElse(0.0));
        vm.setCurAvgFuelUse(Optional.ofNullable(daily.getEndDfConsumption()).orElse(0.0));
        vm.setFuelLevel(Optional.ofNullable(daily.getEndFuelLevel()).orElse(0.0));
        return vm;
    }


    public Map<String, List> analysePower(String vin, ReportVehicleDailyDto dailyDto) throws JsonProcessingException {
        if (!checkExist(vin,"Power Mode")){
            throw new BadRequestException(ErrorConstants.VEHICLE_HAS_NO_FUNCTION);
        }
        List<ReportVehicleDaily> dailies = dailyDao.getData(vin, dailyDto);
        Map<String, Long> powerMap = new HashMap<>(ToolUtil.computeMap(4));
        Map<String, Long> workMap = new HashMap<>(ToolUtil.computeMap(5));
        for (ReportVehicleDaily daily : dailies) {
            //a.解析功率模式
            String pmRatio = daily.getPmRatio();
            if (StringUtils.isNotEmpty(pmRatio)) {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = mapper.readValue(pmRatio, Map.class);
                map.forEach((k, v) -> powerMap.put(k, powerMap.getOrDefault(k, 0L) + this.toLong(v)));
            }
            //b.解析工作模式
            String workModeRatio = daily.getWorkModeRatio();
            if (StringUtils.isNotEmpty(workModeRatio)) {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = mapper.readValue(workModeRatio, Map.class);
                map.forEach((k, v) -> workMap.put(k, workMap.getOrDefault(k, 0L) + this.toLong(v)));
            }
        }
        //结果map转list
        List<Map<String, Object>> powerList = new ArrayList<>(powerMap.size());
        List<Map<String, Object>> workList = new ArrayList<>(workMap.size());
        powerMap.forEach((k, v) -> powerList.add(ImmutableMap.of("name", k, "value", millisecondToHour(v))));
        workMap.forEach((k, v) -> workList.add(ImmutableMap.of("name", k, "value", millisecondToHour(v))));
        return ImmutableMap.of("power", powerList, "work", workList);
    }


    public List<Map<String, Object>> fanController(String vin, ReportVehicleDailyDto dailyDto) {
        if (!checkExist(vin,"Fan Input")){
            throw new BadRequestException(ErrorConstants.VEHICLE_HAS_NO_FUNCTION);
        }
        List<ReportVehicleDaily> dailies = dailyDao.getData(vin, dailyDto);
        Map<String, Long> result = new HashMap<>(ToolUtil.computeMap(16));
        dailies.forEach(daily -> {
            String ratio = daily.getFanStateRatio();
            try {
                if (StringUtils.isNotEmpty(ratio)) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> map = mapper.readValue(ratio, Map.class);
                    map.forEach((k, v) -> result.put(k, result.getOrDefault(k, 0L) + this.toLong(v)));
                }
            } catch (JsonProcessingException e) {
                log.error("fanController json string parse exception: {}", e.getLocalizedMessage());
            }
        });

        List<Map<String, Object>> resultList = new ArrayList<>(result.size());
        result.forEach((k, v) -> resultList.add(ImmutableMap.of("name", k, "value", millisecondToHour(v))));
        return resultList;
    }

    public List<Map<String, Object>> digDrive(String vin, ReportVehicleDailyDto dailyDto) {
        if (!checkExist(vin,"Dig State")){
            throw new BadRequestException(ErrorConstants.VEHICLE_HAS_NO_FUNCTION);
        }
        List<ReportVehicleDaily> dailies = dailyDao.getData(vin, dailyDto);
        Map<String, Long> result = new HashMap<>(ToolUtil.computeMap(6));

        dailies.forEach(daily -> {
            String dig = daily.getDigTravelRatio();
            try {
                if (StringUtils.isNotEmpty(dig)) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> map = mapper.readValue(dig, Map.class);
                    map.forEach((k, v) -> result.put(k, result.getOrDefault(k, 0L) + this.toLong(v)));
                }
            } catch (JsonProcessingException e) {
                log.error("digDrive json string parse exception: {}", e.getLocalizedMessage());
            }
        });

        List<Map<String, Object>> resultList = new ArrayList<>(result.size());
        result.forEach((k, v) -> resultList.add(ImmutableMap.of("name", k, "value", millisecondToHour(v))));
        return resultList;
    }

    private Long toLong(Object obj) {
        return Objects.isNull(obj) ? 0L : Long.parseLong(String.valueOf(obj));
    }

    /**
     * 单车分析页面
     *
     * @param query 查询条件
     * @return 列表
     */
    public PageQuery<ReportVehicleDailyVM> getReportDailyData(ReportVehicleDailyQuery query) {
        if (Objects.isNull(query.getOrganizationId())) {
            query.setOrganizationId(BaseContextHandler.getOrgId());
        }
        PageQuery<ReportVehicleDailyVM> pageQuery = query.toPageQuery();
        dailyDao.getReportDailyData(pageQuery);
        Map<String, List<ReportVehicleDaily>> itemGroupMap = dailyDao.all().stream().collect(Collectors.groupingBy(ReportVehicleDaily::getVin));
        for (ReportVehicleDailyVM reportVehicleDailyVM : pageQuery.getList()) {
            List<ReportVehicleDaily> list = itemGroupMap.get(reportVehicleDailyVM.getVin()).stream().sorted(Comparator.comparing(ReportVehicleDaily::getCreateTime).reversed()).collect(Collectors.toList());
            reportVehicleDailyVM.setEndFuelLevel(list.get(0).getEndFuelLevel());
            reportVehicleDailyVM.setHammerRate(String.format("%.2f", reportVehicleDailyVM.getHammerTime() / reportVehicleDailyVM.getTotalWorkingTime() * 100) + "%");
        }
        return pageQuery;
    }

    /**
     * 发动机转速状态
     *
     * @param vin      机器序列号
     * @param dailyDto 查询条件
     * @return 数据
     */
    public EngineDataVM getEngineData (String vin, ReportVehicleDailyDto dailyDto){
        if (!checkExist(vin,"EngineSpeed")){
            throw new BadRequestException(ErrorConstants.VEHICLE_HAS_NO_FUNCTION);
        }
        List<ReportVehicleDaily> data = dailyDao.getEngineData(vin, dailyDto);
        List<EngineRuntime> runtimeList = new ArrayList<>();
        Map<String, Long> map = new HashMap<>(16);
        Long timeCount = 0L;
        for (ReportVehicleDaily reportVehicleDaily : data) {
            String engineSpeed = reportVehicleDaily.getEngineSpeedRatio();
            Map<String, Long> item = JSONObject.parseObject(engineSpeed, Map.class);
            for (Map.Entry<String, Long> entry : item.entrySet()) {
                if (map.containsKey(entry.getKey())) {
                    map.put(entry.getKey(), map.get(entry.getKey()) + entry.getValue());
                } else {
                    map.put(entry.getKey(), entry.getValue());
                }
                timeCount += entry.getValue();
            }
            EngineRuntime engineRuntime = new EngineRuntime();
            engineRuntime.setDateVal(reportVehicleDaily.getDateVal());
            Double runtime = Double.parseDouble(String.format("%.2f", millisecondToHour(reportVehicleDaily.getEngineRunTime().doubleValue())));
            Double notRuntime = Double.parseDouble(String.format("%.2f", millisecondToHour(reportVehicleDaily.getEngineNotRunTime().doubleValue())));
            engineRuntime.setRuntime(runtime);
            engineRuntime.setNotRuntime(notRuntime);
            runtimeList.add(engineRuntime);
        }
        Double countTime = Double.parseDouble(String.format("%.2f", millisecondToHour(timeCount)));
        List<EngineSpeed> speedList = new ArrayList<>();
        for (Map.Entry<String, Long> entry : map.entrySet()) {
            EngineSpeed engineSpeed = new EngineSpeed();
            engineSpeed.setSpeedRange(entry.getKey());
            Double workTime = Double.parseDouble(String.format("%.2f", millisecondToHour(entry.getValue().doubleValue())));
            engineSpeed.setWorkTime(workTime);
            engineSpeed.setRate(String.format("%.2f", workTime / countTime));
            speedList.add(engineSpeed);
        }
        EngineDataVM engineDataVM = new EngineDataVM();
        engineDataVM.setRuntimeList(runtimeList);
        engineDataVM.setSpeedList(speedList);
        return engineDataVM;
    }

    /**
     * 温度曲线图
     *
     * @param vin      机器序列号
     * @param dailyDto 查询条件
     * @return 温度信息
     */
    public TemperatureInfoVM getTemperatureInfo(String vin, ReportVehicleDailyDto dailyDto) {
        String oilType="Hydraulic Oil Temp";
        if (!checkExist(vin,oilType)){
            oilType="Engine Intake Manifold Temperature";
        }
        String terminalType = apiServiceImp.getTerminalType(vin);
        ArrayList<Map<String, Object>> records = Lists.newArrayList();
        grampusDataQueryService.queryTrackFilterData(terminalType, vin, dailyDto.getStartTime(), dailyDto.getEndTime(), 127, false, null, record -> JSON.parseObject(record.getBody(), Map.class), record -> records.add(record));
        List<DailyTemperature> coolantTempList = new ArrayList<>();
        List<DailyTemperature> hydraulicOilTempList = new ArrayList<>();
        List<DailyTemperature> fuelTempList = new ArrayList<>();
        for (Map<String, Object> record : records) {
            Map<String, Object> objectMap = JSONObject.parseObject(record.get("CONDITION_MSG").toString(), Map.class);
            //获取配置项信息并转化为Map
            List<WorkConditionDto> conditionList = JSONArray.parseArray(objectMap.get("WORK_VEHICLE_PARAMETERS").toString(), WorkConditionDto.class);
            Map<String, Object> tempMap=new HashMap<>(16);
            if (!Objects.isNull(conditionList)) {
                tempMap = conditionList.stream().filter(workConditionDto -> !Objects.isNull(workConditionDto.getNAME())).collect(Collectors.toMap(e -> e.getNAME(), e -> e.getVALUE(), (k1, k2) -> k2));
            }
            long time = Long.parseLong(record.get("TIME").toString());
            LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(time / 1000, 0, ZoneOffset.ofHours(8));
            String dateVal = LocalDateTimeUtils.formatDateTime(localDateTime, null);
            DailyTemperature coolantTemp = new DailyTemperature();
            coolantTemp.setTemperature(tempMap.get("Coolant Temp").toString());
            coolantTemp.setDateVal(dateVal);
            coolantTempList.add(coolantTemp);
            DailyTemperature hydraulicOilTemp = new DailyTemperature();
            hydraulicOilTemp.setTemperature(tempMap.get(oilType).toString());
            hydraulicOilTemp.setDateVal(dateVal);
            hydraulicOilTempList.add(hydraulicOilTemp);
            DailyTemperature fuelTemp = new DailyTemperature();
            fuelTemp.setTemperature(tempMap.get("Fuel Temp").toString());
            fuelTemp.setDateVal(dateVal);
            fuelTempList.add(fuelTemp);
        }
        TemperatureInfoVM temperatureInfoVM = new TemperatureInfoVM();
        temperatureInfoVM.setCoolantTempList(coolantTempList.stream().sorted(Comparator.comparing(DailyTemperature::getDateVal)).collect(Collectors.toList()));
        temperatureInfoVM.setHydraulicOilTempList(hydraulicOilTempList.stream().sorted(Comparator.comparing(DailyTemperature::getDateVal)).collect(Collectors.toList()));
        temperatureInfoVM.setFuelTempList(fuelTempList.stream().sorted(Comparator.comparing(DailyTemperature::getDateVal)).collect(Collectors.toList()));
        return temperatureInfoVM;
    }

    private static double millisecondToHour(Long millisecond) {
        if (millisecond == null) {
            return 0.0;
        }
        return millisecond / (1000 * 60 * 60 * 24.0);
    }

    private static double millisecondToHour(Double millisecond) {
        if (millisecond == null) {
            return 0.0;
        }
        return millisecond / (1000 * 60 * 60 * 24);
    }


    private boolean checkExist(String vin,String itemCode){
        List<FunctionSetItemVM> list=vehicleMonitorDao.getItemListByVin(vin,itemCode);
        if (Objects.isNull(list)){
            return false;
        }
        return true;
    }
}
