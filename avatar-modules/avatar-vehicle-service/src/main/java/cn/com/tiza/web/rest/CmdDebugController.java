package cn.com.tiza.web.rest;

import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.rest.util.ResponseUtil;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cn.com.tiza.domain.CmdDebug;
import cn.com.tiza.service.dto.CmdDebugDto;
import cn.com.tiza.service.dto.CmdDebugQuery;
import cn.com.tiza.service.mapper.CmdDebugMapper;
import cn.com.tiza.web.rest.vm.CmdDebugVM;
import cn.com.tiza.service.CmdDebugService;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Controller
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Slf4j
@RestController
@RequestMapping("/cmdDebug")
public class CmdDebugController {

    @Autowired
    private CmdDebugService cmdDebugService;

    @Autowired
    private CmdDebugMapper cmdDebugMapper;

    @GetMapping
    @Timed
    public ResponseEntity<List<CmdDebugVM>> list(CmdDebugQuery query) {
        if (Objects.isNull(query.getOrganizationId())) {
            query.setOrganizationId(BaseContextHandler.getRootOrgId());
        }
        PageQuery pageQuery = cmdDebugService.findAll(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    @GetMapping("{id}")
    @Timed
    public ResponseEntity<CmdDebugVM> get(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(cmdDebugService.get(id)
                .map(obj -> cmdDebugMapper.toVM(obj)));
    }

    @PostMapping
    @Timed
    public ResponseEntity create(@RequestBody @Valid CmdDebugDto dto) {
        CmdDebug newObj = cmdDebugService.create(dto);
        return ResponseEntity.ok(newObj);
    }

    @PutMapping("{id}")
    @Timed
    public ResponseEntity update(@PathVariable Long id, @RequestBody @Valid CmdDebugDto dto) {
        Optional<CmdDebug> updatedObj = cmdDebugService.update(id, dto);
        return ResponseUtil.wrapOrNotFound(updatedObj);
    }

    @DeleteMapping("{id}")
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cmdDebugService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    @Timed
    public ResponseEntity<Void> delete(@RequestParam Long[] ids) {
        cmdDebugService.delete(ids);
        return ResponseEntity.ok().build();
    }
}
