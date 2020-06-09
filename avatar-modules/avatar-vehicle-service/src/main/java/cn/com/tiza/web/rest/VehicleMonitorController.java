package cn.com.tiza.web.rest;

import cn.com.tiza.rest.ExcelController;
import cn.com.tiza.service.GrampusDataQueryService;
import cn.com.tiza.service.RedisQueryService;
import cn.com.tiza.service.VehicleMonitorService;
import cn.com.tiza.service.VehicleService;
import cn.com.tiza.service.dto.TrackDataQuery;
import cn.com.tiza.service.dto.VehicleMonitorQuery;
import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.rest.vm.*;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author tiza
 */

@Slf4j
@RestController
@RequestMapping("/vehicleMonitor")
public class VehicleMonitorController extends ExcelController {
    @Autowired
    private VehicleMonitorService vehicleMonitorService;

    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private RedisQueryService redisQueryService;

    @Autowired
    private GrampusDataQueryService hbaseService;

    @Autowired
    private AccountApiClient accountApiClient;

    @GetMapping("/getMonitorList")
    @Timed
    public ResponseEntity<List<VehicleMonitorVM>> list(VehicleMonitorQuery query) {
        PageQuery pageQuery = vehicleMonitorService.findAll(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    private String[] titles = {"机器序列号", "终端编号", "SIM卡号", "所属机构", "车辆类型", "车辆型号", "累计工作时间（H）",
            "ACC状态", "定位状态", "终端状态", "最新时间", "最新位置", "锁车状态", "销售状态", "客户名称", "客户电话"};

    @GetMapping("/export")
    public void export(VehicleMonitorQuery query, HttpServletRequest request, HttpServletResponse response) {
        query.setPage(1);
        query.setLimit(Integer.MAX_VALUE);
        int[] permissions = {109611};
        boolean b = accountApiClient.checkPermission(permissions);
        if (!b) {
            ArrayList<String> titleList = new ArrayList<>(Arrays.asList(titles));
            titleList.remove(11);
            titles = titleList.toArray(new String[titleList.size()]);
        }
        List<VehicleMonitorVM> list = vehicleMonitorService.findAll(query).getList();
        download("车辆", titles, list, VehicleMonitorVM::toRow2, request, response);
    }

    /**
     * 地图弹框车辆信息
     */

    @GetMapping("/frameVehicleInfo/{vin}")
    public ResponseEntity<FrameVehicleVM> frameVehicleInfo(@PathVariable String vin) {
        return ResponseEntity.ok(vehicleService.frameVehicleInfo(vin));
    }


    /**
     * 单车信息
     */

    @GetMapping("/monitorVehicleInfo/{code}")
    public ResponseEntity<Map<String, Object>> monitorVehicleInfo(@PathVariable String code) {
        return ResponseEntity.ok(vehicleService.monitorVehicleInfo(code));
    }

    @GetMapping("/trackData/{vin}")
    public ResponseEntity<List<Map>> trackData(@PathVariable String vin, TrackDataQuery query) {
        return ResponseEntity.ok(vehicleService.trackData(vin, query));
    }

    @GetMapping("/getTrackData/{vin}")
    @Timed
    public ResponseEntity<WorkConditionVM> getTrackData(@PathVariable String vin) {
        WorkConditionVM workConditionVM = vehicleMonitorService.getTrackData(vin);
        return ResponseEntity.ok(workConditionVM);
    }

    @GetMapping("/getTrackDataList/{vin}")
    @Timed
    public ResponseEntity<HistoryDataVM> getTrackDataList(@PathVariable String vin, TrackDataQuery query) {
        HistoryDataVM historyDataVM = vehicleMonitorService.getTrackDataList(vin, query);
        return ResponseEntity.ok(historyDataVM);
    }

    @GetMapping("/exportHistoryData")
    public void exportHistoryData(String vin, TrackDataQuery query, HttpServletRequest request, HttpServletResponse response) {
        query.setPage(1);
        query.setPageSize(Integer.MAX_VALUE);
        vehicleMonitorService.exportHistoryData(vin, query, request, response);
    }

    /**
     * 轨迹回放导出
     */
    @GetMapping("/trackDataExport/{vin}")
    public ResponseEntity<List<Map>> trackDataExport(@PathVariable String vin, TrackDataQuery query,
                                                     HttpServletRequest request, HttpServletResponse response) {
        vehicleService.trackDataExport(vin, query, request, response);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getConditionData/{vin}")
    @Timed
    public ResponseEntity<WorkConditionVM> getConditionData(@PathVariable String vin, Long time) {
        WorkConditionVM workConditionVM = vehicleMonitorService.getConditionData(vin, time);
        return ResponseEntity.ok(workConditionVM);
    }

    /**
     * 获取功能集
     */
    @GetMapping("/getFunctionItemListByVin/{vin}")
    public List<FunctionSetItemVM> getFunctionItemListByVin(@PathVariable String vin) {
        return vehicleMonitorService.getItemListByVin(vin);
    }
}
