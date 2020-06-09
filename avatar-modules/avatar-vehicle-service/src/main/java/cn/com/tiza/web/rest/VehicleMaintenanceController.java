package cn.com.tiza.web.rest;

import cn.com.tiza.rest.ExcelController;
import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.rest.util.ResponseUtil;
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

import cn.com.tiza.domain.VehicleMaintenance;
import cn.com.tiza.service.dto.VehicleMaintenanceDto;
import cn.com.tiza.service.dto.VehicleMaintenanceQuery;
import cn.com.tiza.service.mapper.VehicleMaintenanceMapper;
import cn.com.tiza.web.rest.vm.VehicleMaintenanceVM;
import cn.com.tiza.service.VehicleMaintenanceService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
*  Controller
* gen by beetlsql 2020-03-31
* @author tiza
*/
@Slf4j
@RestController
@RequestMapping("/vehicleMaintenance")
public class VehicleMaintenanceController extends ExcelController {

    @Autowired
    private VehicleMaintenanceService vehicleMaintenanceService;

    @Autowired
    private VehicleMaintenanceMapper vehicleMaintenanceMapper;

    @GetMapping
    @Timed
    public ResponseEntity<List<VehicleMaintenanceVM>> list(VehicleMaintenanceQuery query) {
        PageQuery pageQuery = vehicleMaintenanceService.findAll(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    @GetMapping("{id}")
    @Timed
    public ResponseEntity<VehicleMaintenanceVM> get(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(vehicleMaintenanceService.get(id)
                        .map(obj -> vehicleMaintenanceMapper.toVM(obj)));
    }

    @PostMapping
    @Timed
    public ResponseEntity create(@RequestBody @Valid VehicleMaintenanceDto dto) {
        VehicleMaintenance newObj = vehicleMaintenanceService.create(dto);
        return ResponseEntity.ok(newObj);
    }

    @PutMapping("{id}")
    @Timed
    public ResponseEntity update(@PathVariable Long id, @RequestBody @Valid VehicleMaintenanceDto dto) {
        Optional<VehicleMaintenance> updatedObj = vehicleMaintenanceService.update(id, dto);
        return ResponseUtil.wrapOrNotFound(updatedObj);
    }

    @DeleteMapping("{id}")
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vehicleMaintenanceService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    @Timed
    public ResponseEntity<Void> delete(@RequestParam Long[] ids) {
        vehicleMaintenanceService.delete(ids);
        return ResponseEntity.ok().build();
    }

    private String[] titles = {"保养项", "保养内容",  "所属机构", "操作人","操作时间" };

    @GetMapping("/export")
    public void export(VehicleMaintenanceQuery query, HttpServletRequest request, HttpServletResponse response) {
        query.setPage(1);
        query.setLimit(Integer.MAX_VALUE);
        List<VehicleMaintenanceVM> list = vehicleMaintenanceService.findAll(query).getList();
        download("车辆", titles, list, VehicleMaintenanceVM::toRow, request, response);
    }
}
