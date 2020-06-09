package cn.com.tiza.web.rest;

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

import cn.com.tiza.domain.VehicleDebugLog;
import cn.com.tiza.service.dto.VehicleDebugLogDto;
import cn.com.tiza.service.dto.VehicleDebugLogQuery;
import cn.com.tiza.service.mapper.VehicleDebugLogMapper;
import cn.com.tiza.web.rest.vm.VehicleDebugLogVM;
import cn.com.tiza.service.VehicleDebugLogService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
*  Controller
* gen by beetlsql 2020-03-17
* @author tiza
*/
@Slf4j
@RestController
@RequestMapping("/vehicleDebugLog")
public class VehicleDebugLogController {

    @Autowired
    private VehicleDebugLogService vehicleDebugLogService;

    @Autowired
    private VehicleDebugLogMapper vehicleDebugLogMapper;

    @GetMapping
    @Timed
    public ResponseEntity<List<VehicleDebugLogVM>> list(VehicleDebugLogQuery query) {
        PageQuery pageQuery = vehicleDebugLogService.findAll(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(vehicleDebugLogMapper.entitiesToVMList(pageQuery.getList()), headers, HttpStatus.OK);
    }

    @GetMapping("{id}")
    @Timed
    public ResponseEntity<VehicleDebugLogVM> get(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(vehicleDebugLogService.get(id)
                        .map(obj -> vehicleDebugLogMapper.toVM(obj)));
    }

    @PostMapping
    @Timed
    public ResponseEntity create(@RequestBody @Valid VehicleDebugLogDto dto) {
        VehicleDebugLog newObj = vehicleDebugLogService.create(dto);
        return ResponseEntity.ok(newObj);
    }

    @PutMapping("{id}")
    @Timed
    public ResponseEntity update(@PathVariable Long id, @RequestBody @Valid VehicleDebugLogDto dto) {
        Optional<VehicleDebugLog> updatedObj = vehicleDebugLogService.update(id, dto);
        return ResponseUtil.wrapOrNotFound(updatedObj);
    }

    @DeleteMapping("{id}")
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vehicleDebugLogService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    @Timed
    public ResponseEntity<Void> delete(@RequestParam Long[] ids) {
        vehicleDebugLogService.delete(ids);
        return ResponseEntity.ok().build();
    }
}
