package cn.com.tiza.web.rest;

import cn.com.tiza.domain.FunctionSetItemLock;
import cn.com.tiza.service.FunctionSetItemLockService;
import cn.com.tiza.service.dto.FunctionSetItemLockDto;
import cn.com.tiza.service.dto.FunctionSetItemLockQuery;
import cn.com.tiza.service.mapper.FunctionSetItemLockMapper;
import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.rest.util.ResponseUtil;
import cn.com.tiza.web.vm.FunctionSetItemLockVM;
import io.micrometer.core.annotation.Timed;
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
 * gen by beetlsql 2020-03-31
 *
 * @author tiza
 */
@Slf4j
@RestController
@RequestMapping("/function/set/lock")
public class FunctionSetItemLockController {

    @Autowired
    private FunctionSetItemLockService functionSetItemLockService;

    @Autowired
    private FunctionSetItemLockMapper functionSetItemLockMapper;

    @GetMapping
    @SuppressWarnings("unchecked")
    public ResponseEntity<List<FunctionSetItemLockVM>> list(FunctionSetItemLockQuery query) {
        PageQuery<FunctionSetItemLock> pageQuery = functionSetItemLockService.findAll(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(functionSetItemLockMapper.entitiesToVMList(pageQuery.getList()), headers, HttpStatus.OK);
    }

    @GetMapping("{id}")
    @Timed
    public ResponseEntity<FunctionSetItemLockVM> get(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(functionSetItemLockService.get(id)
                .map(obj -> functionSetItemLockMapper.toVM(obj)));
    }

    @PostMapping
    @Timed
    public ResponseEntity create(@RequestBody @Valid FunctionSetItemLockDto dto) {
        FunctionSetItemLock newObj = functionSetItemLockService.create(dto);
        return ResponseEntity.ok(newObj);
    }

    @PutMapping("{id}")
    @Timed
    public ResponseEntity update(@PathVariable Long id, @RequestBody @Valid FunctionSetItemLockDto dto) {
        Optional<FunctionSetItemLock> updatedObj = functionSetItemLockService.update(id, dto);
        return ResponseUtil.wrapOrNotFound(updatedObj);
    }

    @DeleteMapping("{id}")
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        functionSetItemLockService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    @Timed
    public ResponseEntity<Void> delete(@RequestParam Long[] ids) {
        functionSetItemLockService.delete(ids);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/items/{terminalCode}")
    public ResponseEntity<List<FunctionSetItemLock>> getLockOptions(@PathVariable String terminalCode){
        return ResponseEntity.ok(functionSetItemLockService.getLockOptions(terminalCode));
    }
}
