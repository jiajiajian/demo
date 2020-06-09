package cn.com.tiza.web.rest;

import cn.com.tiza.dao.DataAnalyseDao;
import cn.com.tiza.rest.ExcelController;
import cn.com.tiza.service.DataAnalyseService;
import cn.com.tiza.service.dto.DataAnalyseQuery;
import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.rest.vm.*;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author tiza
 */

@Slf4j
@RestController
@RequestMapping("/dataAnalyse")
public class DataAnalyseController extends ExcelController {
    @Autowired
    private DataAnalyseService dataAnalyseService;
    @Autowired
    private DataAnalyseDao dataAnalyseDao;

    /**
     * 区域车辆统计
     * @param dataAnalyseQuery 查询条件
     * @return 查询结果
     */
    @GetMapping("/getCountByProvince")
    @Timed
    public ResponseEntity<List<VehicleAnalyseVM>> getCountByProvince(DataAnalyseQuery dataAnalyseQuery) {
        List<VehicleAnalyseVM> list = dataAnalyseService.getCountByProvince(dataAnalyseQuery);
        return ResponseEntity.ok(list);
    }

    /**
     * 区域车辆统计导出
     */
    @GetMapping("/exportCountByProvince")
    public void export(HttpServletRequest request, HttpServletResponse response, DataAnalyseQuery query) {
        String[] titles = {"区域", "车数"};
        ResponseEntity<List<VehicleAnalyseVM>> list = this.getCountByProvince(query);
        List<VehicleAnalyseVM> body = list.getBody();
        download("区域车辆调试", titles, body, VehicleAnalyseVM::toCountByProvinceRow, request, response);
    }


    /**
     * 机型统计
     * @param dataAnalyseQuery 查询条件
     * @return 查询结果
     */
    @GetMapping("/getCountByVehicleType")
    @Timed
    public ResponseEntity<List<VehicleAnalyseVM>> getCountByVehicleType(DataAnalyseQuery dataAnalyseQuery){
        List<VehicleAnalyseVM> list = dataAnalyseService.getCountByVehicleType(dataAnalyseQuery);
        return ResponseEntity.ok(list);
    }

    /**
     * 机型统计导出
     */
    @GetMapping("/exportCountByVehicleType")
    public void exportCountByVehicleType(HttpServletRequest request, HttpServletResponse response, DataAnalyseQuery query) {
        String[] titles = {"机型", "车数"};
        ResponseEntity<List<VehicleAnalyseVM>> list = this.getCountByVehicleType(query);
        List<VehicleAnalyseVM> body = list.getBody();
        download("机型统计", titles, body, VehicleAnalyseVM::toCountByVehicleTypeRow, request, response);
    }
    /**
     * 总工作小时统计
     * @param dataAnalyseQuery 查询条件
     * @return 查询结果
     */
    @GetMapping("/getTotalWorkTimeByProvince")
    @Timed
    public ResponseEntity<List<VehicleAnalyseVM>> getTotalWorkTimeByProvince(DataAnalyseQuery dataAnalyseQuery){
        List<VehicleAnalyseVM> list = dataAnalyseService.getTotalWorkTimeByProvince(dataAnalyseQuery);
        return ResponseEntity.ok(list);
    }
    /**
     * 总工作小时统计导出
     */
    @GetMapping("/exportTotalWorkTimeByProvince")
    public void exportTotalWorkTimeByProvince(HttpServletRequest request, HttpServletResponse response, DataAnalyseQuery query) {
        String[] titles = {"区域", "总工作小时"};
        ResponseEntity<List<VehicleAnalyseVM>> list = this.getTotalWorkTimeByProvince(query);
        List<VehicleAnalyseVM> body = list.getBody();
        download("总工作小时统计", titles, body, VehicleAnalyseVM::toTotalWorkTimeByProvinceRow, request, response);
    }
    /**
     * 工作时间统计
     * @param dataAnalyseQuery 查询条件
     * @return 查询结果
     */
    @GetMapping("/getWorkTime")
    @Timed
    public ResponseEntity<List<WorkTimeVM>> getWorkTime(DataAnalyseQuery dataAnalyseQuery){
        List<WorkTimeVM> list = dataAnalyseService.getWorkTime(dataAnalyseQuery);
        return ResponseEntity.ok(list);
    }
    /**
     * 工作时间统计导出
     */
    @GetMapping("/exportWorkTime")
    public void exportWorkTime(HttpServletRequest request, HttpServletResponse response, DataAnalyseQuery query) {
        String[] titles = {"机器序列号", "所属机构","车辆类型","车辆型号","累计工作时间(H)","工作时间(H)","最新时间","最新位置"};
        ResponseEntity<List<WorkTimeVM>> list = this.getWorkTime(query);
        List<WorkTimeVM> body = list.getBody();
        download("工作时间统计", titles, body, WorkTimeVM::toRow, request, response);
    }
    /**
     * 工作时间明细
     * @param dataAnalyseQuery 查询条件
     * @return 查询结果
     */
    @GetMapping("/getWorkTimeData")
    @Timed
    public ResponseEntity<WorkTimeData> getWorkTimeData(DataAnalyseQuery dataAnalyseQuery){
        WorkTimeData workTimeData=dataAnalyseService.getWorkTimeData(dataAnalyseQuery);
        return ResponseEntity.ok(workTimeData);
    }

    /**
     * 工作时间明细导出
     * @param dataAnalyseQuery 查询条件
     * @return 查询结果
     */
    @GetMapping("/exportWorkTimeData")
    @Timed
    public void exportWorkTimeData(HttpServletRequest request, HttpServletResponse response,DataAnalyseQuery dataAnalyseQuery){
        String[] titles = {"开始时间", "结束时间","工作时长(H)"};
        List<MachineWorkDetailVM> machineWorkDetailVMList=dataAnalyseDao.getMachineWorkDetail(dataAnalyseQuery);
        download("工作时间明细", titles, machineWorkDetailVMList, MachineWorkDetailVM::toRow, request, response);
    }

    /**
     * 在线率统计
     * @param dataAnalyseQuery 查询条件
     * @return 查询结果
     */
    @GetMapping("/getOnlineRate")
    @Timed
    public ResponseEntity<List<OnlineRateVM>> getOnlineRate(DataAnalyseQuery dataAnalyseQuery){
        List<OnlineRateVM> list =dataAnalyseService.getOnlineRate(dataAnalyseQuery);
        return ResponseEntity.ok(list);
    }

    /**
     * 在线率统计导出
     */
    @GetMapping("/exportOnlineRate")
    public void exportOnlineRate(HttpServletRequest request, HttpServletResponse response, DataAnalyseQuery query) {
        String[] titles = {"统计时间", "在线车辆数","离线车辆数","在线率"};
        ResponseEntity<List<OnlineRateVM>> list = this.getOnlineRate(query);
        List<OnlineRateVM> body = list.getBody();
        download("在线率统计", titles, body, OnlineRateVM::toRow, request, response);
    }

}
