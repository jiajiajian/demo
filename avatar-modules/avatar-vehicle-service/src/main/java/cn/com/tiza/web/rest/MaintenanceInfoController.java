package cn.com.tiza.web.rest;

import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.rest.util.ResponseUtil;
import cn.com.tiza.web.rest.vm.MaintenanceDetailVM;
import com.googlecode.aviator.AviatorEvaluator;
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

import cn.com.tiza.domain.MaintenanceInfo;
import cn.com.tiza.service.dto.MaintenanceInfoDto;
import cn.com.tiza.service.dto.MaintenanceInfoQuery;
import cn.com.tiza.service.mapper.MaintenanceInfoMapper;
import cn.com.tiza.web.rest.vm.MaintenanceInfoVM;
import cn.com.tiza.service.MaintenanceInfoService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
*  Controller
* gen by beetlsql 2020-04-01
* @author tiza
*/
@Slf4j
@RestController
@RequestMapping("/maintenanceInfo")
public class MaintenanceInfoController {

    @Autowired
    private MaintenanceInfoService maintenanceInfoService;

    @Autowired
    private MaintenanceInfoMapper maintenanceInfoMapper;

    @GetMapping("/getInfoListByTacticsId")
    @Timed
    public ResponseEntity<List<MaintenanceDetailVM>> getInfoListByTacticsId(Long tacticsId) {
        List<MaintenanceDetailVM> infoList = maintenanceInfoService.getInfoListByTacticsId(tacticsId);
        return ResponseEntity.ok(infoList);
    }

    @GetMapping("{id}")
    @Timed
    public ResponseEntity<MaintenanceInfoVM> get(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(maintenanceInfoService.get(id)
                        .map(obj -> maintenanceInfoMapper.toVM(obj)));
    }

    @PostMapping
    @Timed
    public ResponseEntity create(@RequestBody @Valid MaintenanceInfoDto dto) {
        List<MaintenanceInfo> newObj = maintenanceInfoService.create(dto);
        return ResponseEntity.ok(newObj);
    }

    @PutMapping("{id}")
    @Timed
    public ResponseEntity update(@PathVariable Long id, @RequestBody @Valid MaintenanceInfoDto dto) {
        Optional<MaintenanceInfo> updatedObj = maintenanceInfoService.update(id, dto);
        return ResponseUtil.wrapOrNotFound(updatedObj);
    }

    @DeleteMapping("{id}")
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        maintenanceInfoService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    @Timed
    public ResponseEntity<Void> delete(@RequestParam Long[] ids) {
        maintenanceInfoService.delete(ids);
        return ResponseEntity.ok().build();
    }
}
