package cn.com.tiza.web.rest.app;

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
import java.util.List;
import java.util.Map;

/**
 * app首页
 * @author tiza
 */

@Slf4j
@RestController
@RequestMapping("/app/dashBoard")
public class AppDashBoardController extends ExcelController {
    @Autowired
    private DashBoardService dashBoardService;
    @Autowired
    private VehicleMonitorService vehicleMonitorService;

    /**
     *app首页   获取车辆总数/当前在线数/李现车辆数
     * @return
     */
    @GetMapping("/getCountByType")
    @Timed
    public ResponseEntity<Map> getCountByType() {
        Map<String,Object> map = dashBoardService.getCountByType();
        return  ResponseEntity.ok(map);
    }

    /**
     * app首页  step 1 获取省市区信息  以及该省下车辆数
     * @param query
     * @return
     */
    @GetMapping("/getCountByProvince")
    @Timed
    public ResponseEntity<List<DashBoardVM>> getCountByProvince(DashBoardQuery query) {
        List<DashBoardVM> list = dashBoardService.getCountByProvince(query);
        return  ResponseEntity.ok(list);
    }

    /**
     * app首页  step 2 获取省市区在地图上的坐标信息
     * @param query
     * @return
     */
    @GetMapping("/getMapInfo")
    @Timed
    public ResponseEntity<List<MapPolyInfo>> getMapInfo(DashBoardQuery query) {
        List<MapPolyInfo> list = dashBoardService.getMapInfo(query);
        return  ResponseEntity.ok(list);
    }


    /**
     * 根据机器序列号/sim卡编号/终端编号查询详细车辆列表
     * @param query
     * @return
     */
    @GetMapping("/getDashBoardList")
    @Timed
    public ResponseEntity<List<VehicleMonitorVM>> list(DashBoardQuery query) {
        PageQuery pageQuery = dashBoardService.getDashBoardList(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }


}
