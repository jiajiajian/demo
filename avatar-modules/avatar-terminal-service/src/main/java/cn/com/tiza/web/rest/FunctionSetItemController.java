package cn.com.tiza.web.rest;

import cn.com.tiza.domain.FunctionSetItem;
import cn.com.tiza.service.FunctionSetItemService;
import cn.com.tiza.service.dto.FunctionSetItemDto;
import cn.com.tiza.service.dto.FunctionSetItemQuery;
import cn.com.tiza.service.mapper.FunctionSetItemMapper;
import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.rest.util.ResponseUtil;
import cn.com.tiza.web.vm.FunctionSetItemVM;
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
@RequestMapping("/function/set/item")
public class FunctionSetItemController {

    @Autowired
    private FunctionSetItemService functionSetItemService;

    @Autowired
    private FunctionSetItemMapper functionSetItemMapper;

    @GetMapping
    @SuppressWarnings("unchecked")
    public ResponseEntity<List<FunctionSetItemVM>> list(@Valid FunctionSetItemQuery query) {
        PageQuery pageQuery = functionSetItemService.findAll(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(functionSetItemMapper.entitiesToVMList(pageQuery.getList()), headers, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<FunctionSetItemVM> get(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(functionSetItemService.get(id)
                .map(obj -> functionSetItemMapper.toVM(obj)));
    }

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid FunctionSetItemDto dto) {
        FunctionSetItem newObj = functionSetItemService.create(dto);
        return ResponseEntity.ok(newObj);
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody @Valid FunctionSetItemDto dto) {
        Optional<FunctionSetItem> updatedObj = functionSetItemService.update(id, dto);
        return ResponseUtil.wrapOrNotFound(updatedObj);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        functionSetItemService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<Void> delete(@RequestParam Long[] ids) {
        functionSetItemService.delete(ids);
        return ResponseEntity.ok().build();
    }
}
