package cn.com.tiza.web.rest;

import cn.com.tiza.rest.ExcelController;
import cn.com.tiza.service.DashBoardService;
import cn.com.tiza.service.VehicleMonitorService;
import cn.com.tiza.service.dto.DashBoardQuery;
import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.rest.vm.DashBoardVM;
import cn.com.tiza.web.rest.vm.MapPolyInfo;
import cn.com.tiza.web.rest.vm.VehicleMonitorVM;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tiza
 */

@Slf4j
@RestController
@RequestMapping("/dashBoard")
public class DashBoardController extends ExcelController {
    @Autowired
    private DashBoardService dashBoardService;
    @Autowired
    private VehicleMonitorService vehicleMonitorService;
    @GetMapping("/getDashBoardList")
    @Timed
    public ResponseEntity<List<VehicleMonitorVM>> list(DashBoardQuery query) {
        PageQuery pageQuery = dashBoardService.getDashBoardList(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    @GetMapping("/getMapInfo")
    @Timed
    public ResponseEntity<List<MapPolyInfo>> getMapInfo(DashBoardQuery query) {
        List<MapPolyInfo> list = dashBoardService.getMapInfo(query);
        return  ResponseEntity.ok(list);
    }

    @GetMapping("/getCountByProvince")
    @Timed
    public ResponseEntity<List<DashBoardVM>> getCountByProvince(DashBoardQuery query) {
        List<DashBoardVM> list = dashBoardService.getCountByProvince(query);
        return  ResponseEntity.ok(list);
    }

    @GetMapping("/getCountByType")
    @Timed
    public ResponseEntity<Map> getCountByType() {
        Map<String,Object> map = dashBoardService.getCountByType();
        return  ResponseEntity.ok(map);
    }

    @GetMapping("/getVehicleInfoByVin")
    @Timed
    public ResponseEntity<VehicleMonitorVM> getVehicleInfoByVin(String vin) {
        return ResponseEntity.ok(dashBoardService.getVehicleInfoByVin(vin));
    }

    private String[] titles = {"机器序列号", "终端编号", "所属机构", "车辆类型", "车辆型号", "终端状态", "最新时间", "GPS位置"};

    @GetMapping("/export")
    public void export(DashBoardQuery query, HttpServletRequest request, HttpServletResponse response) {
        query.setPage(1);
        query.setLimit(Integer.MAX_VALUE);
        List<VehicleMonitorVM> list = dashBoardService.getDashBoardList(query).getList();
        download("车辆", titles, list, VehicleMonitorVM::toDashBoardRow, request, response);
    }
}
