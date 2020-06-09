package cn.com.tiza.web.rest;

import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.domain.SoftVersion;
import cn.com.tiza.dto.SelectOption;
import cn.com.tiza.service.SoftVersionService;
import cn.com.tiza.service.dto.SoftVersionDto;
import cn.com.tiza.service.dto.SoftVersionQuery;
import cn.com.tiza.service.mapper.SoftVersionMapper;
import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.rest.util.ResponseUtil;
import cn.com.tiza.web.vm.SoftVersionVM;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Controller
 * gen by beetlsql 2020-03-09
 *
 * @author tiza
 */
@Slf4j
@RestController
@RequestMapping("/soft/version")
public class SoftVersionController {

    @Autowired
    private SoftVersionService softVersionService;

    @Autowired
    private SoftVersionMapper softVersionMapper;

    @GetMapping
    public ResponseEntity<List<SoftVersionVM>> list(SoftVersionQuery query) {
        PageQuery pageQuery = softVersionService.findAll(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(softVersionMapper.entitiesToVMList(pageQuery.getList()), headers, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<SoftVersionVM> get(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(softVersionService.get(id)
                .map(obj -> softVersionMapper.toVM(obj)));
    }

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid SoftVersionDto dto) {
        softVersionService.checkUnique(dto.getCode());
        SoftVersion newObj = softVersionService.create(dto);
        return ResponseEntity.ok(newObj);
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody @Valid SoftVersionDto dto) {
        softVersionService.checkUnique(id, dto.getCode());
        Optional<SoftVersion> updatedObj = softVersionService.update(id, dto);
        return ResponseUtil.wrapOrNotFound(updatedObj);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        softVersionService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<Void> delete(@RequestParam Long[] ids) {
        softVersionService.delete(ids);
        return ResponseEntity.ok().build();
    }

    @GetMapping("options")
    public ResponseEntity<List<SelectOption>> getSoftVersions() {
        return ResponseEntity.ok(softVersionService.getVersions());
    }
}
