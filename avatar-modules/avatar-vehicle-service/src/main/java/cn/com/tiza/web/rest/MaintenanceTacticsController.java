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

import cn.com.tiza.domain.MaintenanceTactics;
import cn.com.tiza.service.dto.MaintenanceTacticsDto;
import cn.com.tiza.service.dto.MaintenanceTacticsQuery;
import cn.com.tiza.service.mapper.MaintenanceTacticsMapper;
import cn.com.tiza.web.rest.vm.MaintenanceTacticsVM;
import cn.com.tiza.service.MaintenanceTacticsService;

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
@RequestMapping("/maintenanceTactics")
public class MaintenanceTacticsController {

    @Autowired
    private MaintenanceTacticsService maintenanceTacticsService;

    @Autowired
    private MaintenanceTacticsMapper maintenanceTacticsMapper;

    @GetMapping
    @Timed
    public ResponseEntity<List<MaintenanceTacticsVM>> list(MaintenanceTacticsQuery query) {
        PageQuery pageQuery = maintenanceTacticsService.findAll(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(maintenanceTacticsMapper.entitiesToVMList(pageQuery.getList()), headers, HttpStatus.OK);
    }

    @GetMapping("{id}")
    @Timed
    public ResponseEntity<MaintenanceTacticsVM> get(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(maintenanceTacticsService.get(id)
                        .map(obj -> maintenanceTacticsMapper.toVM(obj)));
    }

    @GetMapping("getDetail/{id}")
    @Timed
    public ResponseEntity<MaintenanceTacticsVM> getDetail(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(maintenanceTacticsService.getDetail(id));
    }

    @PostMapping
    @Timed
    public ResponseEntity create(@RequestBody @Valid MaintenanceTacticsDto dto) {
        MaintenanceTactics newObj = maintenanceTacticsService.create(dto);
        return ResponseEntity.ok(newObj);
    }

    @PutMapping("{id}")
    @Timed
    public ResponseEntity update(@PathVariable Long id, @RequestBody @Valid MaintenanceTacticsDto dto) {
        Optional<MaintenanceTactics> updatedObj = maintenanceTacticsService.update(id, dto);
        return ResponseUtil.wrapOrNotFound(updatedObj);
    }

    @DeleteMapping("{id}")
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        maintenanceTacticsService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    @Timed
    public ResponseEntity<Void> delete(@RequestParam Long[] ids) {
        maintenanceTacticsService.delete(ids);
        return ResponseEntity.ok().build();
    }
}
