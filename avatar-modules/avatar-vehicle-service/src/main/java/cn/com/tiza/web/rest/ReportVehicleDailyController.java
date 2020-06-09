package cn.com.tiza.web.rest;

import cn.com.tiza.rest.ExcelController;
import cn.com.tiza.service.ReportVehicleDailyService;
import cn.com.tiza.service.dto.ReportVehicleDailyDto;
import cn.com.tiza.service.dto.ReportVehicleDailyQuery;
import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.rest.vm.EngineDataVM;
import cn.com.tiza.web.rest.vm.ReportVehicleDailyFuelVM;
import cn.com.tiza.web.rest.vm.ReportVehicleDailyVM;
import cn.com.tiza.web.rest.vm.TemperatureInfoVM;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class ReportVehicleDailyController extends ExcelController {

    @Autowired
    private ReportVehicleDailyService dailyService;


    @GetMapping("/report/{vin}/analyse/fuel")
    public ResponseEntity<List<ReportVehicleDailyFuelVM>> analyseFuelLevel(@PathVariable String vin,
                                                                           @Valid ReportVehicleDailyDto dailyDto) {

        List<ReportVehicleDailyFuelVM> result = dailyService.analyseFuelLevel(vin, dailyDto);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/report/{vin}/analyse/power")
    public ResponseEntity<Map<String, List>> analysePower(@PathVariable String vin,
                                                         @Valid ReportVehicleDailyDto dailyDto) {

        Map<String, List> result = new HashMap<>(0);
        try {
            result = dailyService.analysePower(vin, dailyDto);
        } catch (JsonProcessingException e) {
            log.error("analysePower json 字符串解析异常： {}", e.getLocalizedMessage());
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/report/{vin}/analyse/fan")
    public ResponseEntity<List<Map<String, Object>>> analyseFanController(@PathVariable String vin,
                                                                   @Valid ReportVehicleDailyDto dailyDto) {
        return ResponseEntity.ok(dailyService.fanController(vin, dailyDto));
    }

    @GetMapping("/report/{vin}/analyse/dig")
    public ResponseEntity<List<Map<String, Object>>> analyseDigDrive(@PathVariable String vin,
                                                                   @Valid ReportVehicleDailyDto dailyDto) {
        return ResponseEntity.ok(dailyService.digDrive(vin, dailyDto));
    }

    @GetMapping("/report/getList")
    public ResponseEntity<List<ReportVehicleDailyVM>> getList(ReportVehicleDailyQuery query) {
        PageQuery pageQuery = dailyService.getReportDailyData(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    @GetMapping("/report/getEngineData")
    public ResponseEntity<EngineDataVM> getEngineData(String vin, ReportVehicleDailyDto dailyDto) {
        return ResponseEntity.ok(dailyService.getEngineData(vin, dailyDto));
    }

    @GetMapping("/report/getTemperatureInfo")
    public ResponseEntity<TemperatureInfoVM> getTemperatureInfo(String vin, ReportVehicleDailyDto dailyDto) {
        return ResponseEntity.ok(dailyService.getTemperatureInfo(vin, dailyDto));
    }

    @GetMapping("/report/export")
    public void export(ReportVehicleDailyQuery query, HttpServletRequest request, HttpServletResponse response){
        String []titles = {"机器序列号","所属机构","车辆类型","车辆型号","工作时长(H)","锤击时长(H)","锤击百分比(%)",
                "发动机运转时长(H)", "发动机未运转时长(H)","最大冷却液温度(℃)","最大液压油温度(℃)","最大进气管温度(℃)",
                "最后平均油耗（升/时）","最后当前平均油耗（升/时）","最后燃油油位"};
        List<ReportVehicleDailyVM> list = dailyService.getReportDailyData(query).getList();
        query.setPage(1);
        query.setLimit(Integer.MAX_VALUE);
        download("单车分析", titles, list, ReportVehicleDailyVM::toRow, request, response);
    }
}
