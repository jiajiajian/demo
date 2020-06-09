package cn.com.tiza.web.rest.app;

import cn.com.tiza.dao.VehicleDao;
import cn.com.tiza.dao.VehicleMonitorDao;
import cn.com.tiza.domain.Vehicle;
import cn.com.tiza.domain.VehicleRealtime;
import cn.com.tiza.excel.read.ExcelReader;
import cn.com.tiza.rest.ExcelController;
import cn.com.tiza.service.RedisQueryService;
import cn.com.tiza.service.VehicleMonitorService;
import cn.com.tiza.service.VehicleRealtimeService;
import cn.com.tiza.service.VehicleService;
import cn.com.tiza.service.dto.*;
import cn.com.tiza.service.mapper.VehicleMapper;
import cn.com.tiza.service.mapper.VehicleRealtimeMapper;
import cn.com.tiza.web.rest.FileApiClient;
import cn.com.tiza.web.rest.errors.BadRequestException;
import cn.com.tiza.web.rest.errors.ErrorConstants;
import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.rest.util.ResponseUtil;
import cn.com.tiza.web.rest.vm.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.netflix.discovery.converters.Auto;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Slf4j
@RestController
@RequestMapping("/app/vehicle")
public class AppVehicleController{

    @Autowired
    private VehicleMonitorService vehicleMonitorService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private VehicleMonitorDao vehicleMonitorDao;

    @Autowired
    private VehicleRealtimeService vehicleRealtimeService;

    @Autowired
    private VehicleRealtimeMapper vehicleRealtimeMapper;

    /**
     * 车辆列表
     * @param query
     * @return
     */
    @GetMapping
    @Timed
    public ResponseEntity<List<VehicleMonitorVM>> list(VehicleMonitorQuery query) {
        PageQuery pageQuery = vehicleMonitorService.findByCon(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    /**
     * 车辆实时信息
     * @param code
     * @return
     */
    @GetMapping("/monitorVehicleRealTimeInfo/{code}")
    public ResponseEntity<VehicleRealtimeVM> monitorVehicleRealTimeInfo(@PathVariable String code) {
        return ResponseEntity.ok(vehicleService.monitorVehicleRealTimeInfo(code));
    }

    /**
     * 车辆基本信息
     * @param code
     * @return
     */
    @GetMapping("/monitorVehicleBaseInfo/{code}")
    public ResponseEntity<VehicleVM> monitorVehicleBaseInfo(@PathVariable String code) {
        return ResponseEntity.ok(vehicleService.monitorVehicleBaseInfo(code));
    }

    /**
     * 工作参数
     * @param vin
     * @return
     */
    @GetMapping("/getTrackData/{vin}")
    public ResponseEntity<WorkConditionVM> getTrackData(@PathVariable @NotNull  String vin) {
        WorkConditionVM workConditionVM = vehicleMonitorService.getTrackData(vin);
        return  ResponseEntity.ok(workConditionVM);
    }

    /**
     * 车辆轨迹
     * @param vin
     * @param query
     * @return
     */
    @GetMapping("/trackData/{vin}")
    public ResponseEntity<List<Map>> trackData(@PathVariable String vin, TrackDataQuery query) {
        return ResponseEntity.ok(vehicleService.trackData(vin, query));
    }

    @GetMapping("/getTrackDataList/{vin}")
    public ResponseEntity<List<WorkConditionVM>> getTrackDataList(@PathVariable String vin,TrackDataQuery query) {
        List<WorkConditionVM> list = vehicleMonitorService.getTrackDataList(vin,query).getConditionVMList();
        return  ResponseEntity.ok(list);
    }

    /**
     * 基本信息
     * @param id
     * @return
     */
    @GetMapping("/info/{id}")
    public ResponseEntity<VehicleVM> info(@PathVariable @NotNull  Long id) {
        VehicleVM vehicleVM = vehicleMonitorDao.getVehicleBaseInfo(id);
        return ResponseEntity.ok(vehicleVM);
    }
}
