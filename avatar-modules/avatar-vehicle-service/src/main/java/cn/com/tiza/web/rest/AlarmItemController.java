package cn.com.tiza.web.rest;

import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.domain.AlarmItem;
import cn.com.tiza.service.AlarmItemService;
import cn.com.tiza.service.dto.AlarmItemDto;
import cn.com.tiza.service.dto.AlarmItemQuery;
import cn.com.tiza.service.mapper.AlarmItemMapper;
import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.rest.util.ResponseUtil;
import cn.com.tiza.web.rest.vm.AlarmItemVM;
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
import java.util.Objects;
import java.util.Optional;

/**
*  Controller
* gen by beetlsql 2020-03-10
* @author tiza
*/
@Slf4j
@RestController
@RequestMapping("/alarmItem")
public class AlarmItemController {

    @Autowired
    private AlarmItemService alarmItemService;

    @Autowired
    private AlarmItemMapper alarmItemMapper;

    @GetMapping
    @Timed
    public ResponseEntity<List<AlarmItemVM>> list(AlarmItemQuery query) {
        if(Objects.isNull(query.getOrganizationId())){
            query.setOrganizationId(BaseContextHandler.getOrgId());
        }
        PageQuery pageQuery = alarmItemService.findAll(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    @GetMapping("{id}")
    @Timed
    public ResponseEntity<AlarmItemVM> get(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(alarmItemService.get(id)
                        .map(obj -> alarmItemMapper.toVM(obj)));
    }

    @PostMapping
    @Timed
    public ResponseEntity create(@RequestBody @Valid AlarmItemDto dto) {
        AlarmItem newObj = alarmItemService.create(dto);
        return ResponseEntity.ok(newObj);
    }

    @PutMapping("{id}")
    @Timed
    public ResponseEntity update(@PathVariable Long id, @RequestBody @Valid AlarmItemDto dto) {
        Optional<AlarmItem> updatedObj = alarmItemService.update(id, dto);
        return ResponseUtil.wrapOrNotFound(updatedObj);
    }

    @DeleteMapping("{id}")
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        alarmItemService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    @Timed
    public ResponseEntity<Void> delete(@RequestParam Long[] ids) {
        alarmItemService.delete(ids);
        return ResponseEntity.ok().build();
    }
}
