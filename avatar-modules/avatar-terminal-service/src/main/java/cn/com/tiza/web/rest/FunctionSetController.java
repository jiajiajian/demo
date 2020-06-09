package cn.com.tiza.web.rest;

import cn.com.tiza.domain.FunctionSet;
import cn.com.tiza.dto.SelectOption;
import cn.com.tiza.service.FunctionSetService;
import cn.com.tiza.service.dto.FunctionSetDto;
import cn.com.tiza.service.dto.FunctionSetQuery;
import cn.com.tiza.service.mapper.FunctionSetMapper;
import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.rest.util.ResponseUtil;
import cn.com.tiza.web.vm.FunctionSetVM;
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
 * gen by beetlsql 2020-03-09
 *
 * @author tiza
 */
@Slf4j
@RestController
@RequestMapping("/function/set")
public class FunctionSetController {

    @Autowired
    private FunctionSetService functionSetService;

    @Autowired
    private FunctionSetMapper functionSetMapper;

    @GetMapping
    @SuppressWarnings("unchecked")
    public ResponseEntity<List<FunctionSetVM>> list(FunctionSetQuery query) {
        PageQuery pageQuery = functionSetService.findAll(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(functionSetMapper.entitiesToVMList(pageQuery.getList()), headers, HttpStatus.OK);
    }

    @GetMapping("{id}")
    @Timed
    public ResponseEntity<FunctionSetVM> get(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(functionSetService.get(id)
                .map(obj -> functionSetMapper.toVM(obj)));
    }

    @PostMapping
    @Timed
    public ResponseEntity create(@RequestBody @Valid FunctionSetDto dto) {
        FunctionSet newObj = functionSetService.create(dto);
        return ResponseEntity.ok(newObj);
    }

    @PutMapping("{id}")
    @Timed
    public ResponseEntity update(@PathVariable Long id, @RequestBody @Valid FunctionSetDto dto) {
        Optional<FunctionSet> updatedObj = functionSetService.update(id, dto);
        return ResponseUtil.wrapOrNotFound(updatedObj);
    }

    @DeleteMapping("{id}")
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        functionSetService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    @Timed
    public ResponseEntity<Void> delete(@RequestParam Long[] ids) {
        functionSetService.delete(ids);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{type}/options")
    public ResponseEntity<List<SelectOption>> getFunctionSets(@PathVariable Integer type){
        return ResponseEntity.ok(functionSetService.getFunctionSets(type));
    }
}
