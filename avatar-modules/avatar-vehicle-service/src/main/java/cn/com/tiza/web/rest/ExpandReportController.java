package cn.com.tiza.web.rest;

import cn.com.tiza.rest.ExcelController;
import cn.com.tiza.service.ExpandReportService;
import cn.com.tiza.service.dto.AvgModelOilConsumptionQuery;
import cn.com.tiza.service.dto.AvgOilConsumptionQuery;
import cn.com.tiza.service.dto.MonthAvgWorkTimeQuery;
import cn.com.tiza.service.dto.TonnageMonthVehicleNumQuery;
import cn.com.tiza.web.rest.vm.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @author tz0920
 */
@Slf4j
@RestController
@RequestMapping("/expandReport")
public class ExpandReportController extends ExcelController {
    @Autowired
    private ExpandReportService expandReportService;

    /**
     * 月工时统计
     *
     * @param query
     * @return
     */
    @GetMapping("/monthAvgWorkTime")
    public ResponseEntity<List<MonthWorkTimeChartVM>> monthAvgWorkTime(@Valid MonthAvgWorkTimeQuery query) {
        return ResponseEntity.ok(expandReportService.monthAvgWorkTime(query));
    }

    /**
     * 活跃车辆分布统计
     *
     * @param month
     * @return
     */
    @GetMapping("/activeVehicleDistribute/{month}")
    public ResponseEntity<List<ActiveNumDistributeVM>> activeVehicleDistribute(@PathVariable Integer month) {
        return ResponseEntity.ok(expandReportService.activeVehicleDistribute(month));
    }

    private String[] titles = {"省份", "10T", "20T", "30T", "40T"};

    @GetMapping("/activeVehicleDistribute/{month}/export")
    public void activeVehicleDistributeExport(@PathVariable Integer month, HttpServletRequest request, HttpServletResponse response) {
        List<ActiveNumDistributeVM> vms = expandReportService.activeVehicleDistribute(month);
        download("活跃车辆分布统计导出", titles, vms, ActiveNumDistributeVM::toRow, request, response);
    }

    /**
     * 车辆油耗分布
     *
     * @param query
     * @return
     */
    @GetMapping("/avgOilConsumption")
    public ResponseEntity<List<AvgOilConsumptionVM>> avgOilConsumption(AvgOilConsumptionQuery query) {
        return ResponseEntity.ok(expandReportService.avgOilConsumption(query));
    }


    private String[] avgOilConsumptionTitles = {"机器序列号", "终端编号", "车辆类型", "车辆型号", "车辆当前平均油耗(升/时)", "车型平均油耗(升/时)"};

    @GetMapping("/avgOilConsumptionExport")
    public void avgOilConsumptionExport(AvgOilConsumptionQuery query, HttpServletRequest request, HttpServletResponse response) {
        List<AvgOilConsumptionVM> vms = expandReportService.avgOilConsumption(query);
        download("车辆油耗分布", avgOilConsumptionTitles, vms, AvgOilConsumptionVM::toRow, request, response);
    }


    /**
     * 机型油耗分布
     * @param query
     * @return
     */
    @GetMapping("/model/avgOilConsumption")
    public ResponseEntity<List<AvgModelOilConsumptionVM>> avgModelOilConsumption(AvgModelOilConsumptionQuery query) {
        return ResponseEntity.ok(expandReportService.avgModelOilConsumption(query));
    }

    /**
     * 机型油耗导出
     * @param query
     * @param request
     * @param response
     */
    @GetMapping("/model/avgOilConsumptionExport")
    public void avgOilConsumptionExport(AvgModelOilConsumptionQuery query, HttpServletRequest request, HttpServletResponse response) {
        List<AvgModelOilConsumptionVM> vms = expandReportService.avgModelOilConsumption(query);
        String[] avgOilConsumptionTitles = {"车辆型号", "车辆数", "车型油耗平均值(升/时)", "车辆型号", "车型油耗最大值(升/时)", "车型油耗最小值(升/时)"};
        download("机型油耗分布", avgOilConsumptionTitles, vms, AvgModelOilConsumptionVM::toRow, request, response);
    }

    /**
     * 各吨位车辆数统计
     * @param query
     * @return
     */
    @GetMapping("monthTonnageVehicleNum")
    public ResponseEntity<List<TonnageMonthVehicleNumVM>> monthTonnageVehicleNum(@Valid TonnageMonthVehicleNumQuery query) {
        return ResponseEntity.ok(expandReportService.monthTonnageVehicleNum(query));
    }


    @GetMapping("tonnage/monthVehicleNumExport")
    public void monthVehicleNumExport(@Valid TonnageMonthVehicleNumQuery query,HttpServletRequest request, HttpServletResponse response) {
        List<TonnageMonthVehicleNumVM> vms = expandReportService.monthTonnageVehicleNum(query);
        String[] avgOilConsumptionTitles = {"月份", "吨位", "0-30h", "30-240h", "240-500h", ">500h"};
        download("各吨位小时数统计", avgOilConsumptionTitles, vms, TonnageMonthVehicleNumVM::toRow, request, response);
    }

}
