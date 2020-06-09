package cn.com.tiza.web.rest;

import cn.com.tiza.rest.ExcelController;
import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.rest.util.ResponseUtil;
import cn.com.tiza.web.rest.vm.VehicleVM;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cn.com.tiza.domain.VehicleDebug;
import cn.com.tiza.service.dto.VehicleDebugDto;
import cn.com.tiza.service.dto.VehicleDebugQuery;
import cn.com.tiza.service.mapper.VehicleDebugMapper;
import cn.com.tiza.web.rest.vm.VehicleDebugVM;
import cn.com.tiza.service.VehicleDebugService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

/**
*  Controller
* gen by beetlsql 2020-03-17
* @author tiza
*/
@Slf4j
@RestController
@RequestMapping("/vehicleDebug")
public class VehicleDebugController extends ExcelController {

    @Autowired
    private VehicleDebugService vehicleDebugService;

    @Autowired
    private VehicleDebugMapper vehicleDebugMapper;

    @Autowired
    private AccountApiClient accountApiClient;

    @GetMapping
    @Timed
    public ResponseEntity<List<VehicleDebugVM>> list(VehicleDebugQuery query) {
        PageQuery pageQuery = vehicleDebugService.findAll(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    @GetMapping("/getVehicleList")
    @Timed
    public ResponseEntity<List<VehicleVM>> getVehicleList(VehicleDebugQuery query) {
        PageQuery pageQuery = vehicleDebugService.getVehicleList(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    @GetMapping("/getVehicleDebugList")
    @Timed
    public ResponseEntity<List<VehicleDebugVM>> getVehicleDebugList(VehicleDebugQuery query) {
        PageQuery pageQuery = vehicleDebugService.getVehicleDebugLogList(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    @GetMapping("{id}")
    @Timed
    public ResponseEntity<VehicleDebugVM> get(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(vehicleDebugService.getVehicleDebugById(id));
    }

    @PostMapping
    @Timed
    public ResponseEntity<Void> create(@RequestParam String[] vins) {
         vehicleDebugService.create(vins);
        return ResponseEntity.ok().build();
    }

    @GetMapping("getCountByStatus")
    @Timed
    public ResponseEntity<HashMap> getCountByStatus() {
        return ResponseUtil.wrapOrNotFound(vehicleDebugService.getCountByStatus());
    }


    @GetMapping("/export")
    public void export(HttpServletRequest request, HttpServletResponse response, VehicleDebugQuery query) {
         String[] titles = {"机器序列号", "终端编号", "SIM卡号", "调试结果",
                "所属机构","车辆类型","车辆型号","ACC状态","定位状态","终端状态","锁车状态","调试时间","调试位置","调试人","调试状态"};
        query.setLimit(Integer.MAX_VALUE - 1);
        query.setPage(1);
        int[] permissions = {109611};
        boolean b = accountApiClient.checkPermission(permissions);
        if (!b) {
            ArrayList<String> titleList = new ArrayList<>(Arrays.asList(titles));
            titleList.remove(12);
            titles = titleList.toArray(new String[titleList.size()]);
        }
        ResponseEntity<List<VehicleDebugVM>> list = this.list(query);
        List<VehicleDebugVM> body = list.getBody();
        download("终端调试", titles, body, VehicleDebugVM::toRow2, request, response);
    }

    @GetMapping("/exportLog")
    public void exportLog(HttpServletRequest request, HttpServletResponse response, VehicleDebugQuery query) {
        String[] titles = {"机器序列号", "终端编号", "SIM卡号", "调试结果",
                "所属机构","车辆类型","车辆型号","ACC状态","定位状态","终端状态","锁车状态","调试时间","调试位置","调试人","调试状态"};
        query.setLimit(Integer.MAX_VALUE - 1);
        query.setPage(1);
        int[] permissions = {109611};
        boolean b = accountApiClient.checkPermission(permissions);
        if (!b) {
            ArrayList<String> titleList = new ArrayList<>(Arrays.asList(titles));
            titleList.remove(12);
            titles = titleList.toArray(new String[titleList.size()]);
        }
        ResponseEntity<List<VehicleDebugVM>> list = this.getVehicleDebugList(query);
        List<VehicleDebugVM> body = list.getBody();
        download("终端调试日志", titles, body, VehicleDebugVM::toRow2, request, response);
    }


    @PutMapping("{id}")
    @Timed
    public ResponseEntity update(@PathVariable Long id, @RequestBody @Valid VehicleDebugDto dto) {
        Optional<VehicleDebug> updatedObj = vehicleDebugService.update(id, dto);
        return ResponseUtil.wrapOrNotFound(updatedObj);
    }

    @DeleteMapping("{id}")
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vehicleDebugService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    @Timed
    public ResponseEntity<Void> delete(@RequestParam Long[] ids) {
        vehicleDebugService.delete(ids);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/vehicleTestServiceJob")
    public void vehicleTestServiceJob() {
        vehicleDebugService.vehicleTestServiceJob();
    }
}
