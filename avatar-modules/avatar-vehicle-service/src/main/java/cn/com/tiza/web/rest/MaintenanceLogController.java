package cn.com.tiza.web.rest;

import cn.com.tiza.context.BaseContextHandler;
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

import cn.com.tiza.domain.MaintenanceLog;
import cn.com.tiza.service.dto.MaintenanceLogDto;
import cn.com.tiza.service.dto.MaintenanceLogQuery;
import cn.com.tiza.service.mapper.MaintenanceLogMapper;
import cn.com.tiza.web.rest.vm.MaintenanceLogVM;
import cn.com.tiza.service.MaintenanceLogService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
*  Controller
* gen by beetlsql 2020-03-31
* @author tiza
*/
@Slf4j
@RestController
@RequestMapping("/maintenanceLog")
public class MaintenanceLogController {

    @Autowired
    private MaintenanceLogService maintenanceLogService;

    @Autowired
    private MaintenanceLogMapper maintenanceLogMapper;

    @GetMapping
    @Timed
    public ResponseEntity<List<MaintenanceLogVM>> list(MaintenanceLogQuery query) {
        if (Objects.isNull(query.getOrganizationId())){
            query.setOrganizationId(BaseContextHandler.getOrgId());
        }
        PageQuery pageQuery = maintenanceLogService.findAll(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    @GetMapping("{id}")
    @Timed
    public ResponseEntity<MaintenanceLogVM> get(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(maintenanceLogService.get(id)
                        .map(obj -> maintenanceLogMapper.toVM(obj)));
    }

    @GetMapping("getDetail")
    @Timed
    public ResponseEntity<MaintenanceLogVM> getDetail(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(maintenanceLogService.getLogDetail(id));
    }

    @PostMapping
    @Timed
    public ResponseEntity create(@RequestBody @Valid MaintenanceLogDto dto) {
        MaintenanceLog newObj = maintenanceLogService.create(dto);
        return ResponseEntity.ok(newObj);
    }

    @PutMapping("{id}")
    @Timed
    public ResponseEntity update(@PathVariable Long id, @RequestBody @Valid MaintenanceLogDto dto) {
        Optional<MaintenanceLog> updatedObj = maintenanceLogService.update(id, dto);
        return ResponseUtil.wrapOrNotFound(updatedObj);
    }

    @DeleteMapping("{id}")
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        maintenanceLogService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    @Timed
    public ResponseEntity<Void> delete(@RequestParam Long[] ids) {
        maintenanceLogService.delete(ids);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Integer>> count() {
        return ResponseEntity.ok(maintenanceLogService.count());
    }

    @GetMapping("/produceMaintenanceLog")
    public void produceMaintenanceLog() {
        maintenanceLogService.produceMaintenanceLog();
    }

}
