package cn.com.tiza.web.rest;

import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.domain.LockApply;
import cn.com.tiza.service.LockApplyService;
import cn.com.tiza.service.LockApplyVinService;
import cn.com.tiza.service.dto.LockApplyDto;
import cn.com.tiza.service.dto.LockApplyQuery;
import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.rest.util.ResponseUtil;
import cn.com.tiza.web.rest.vm.LockApplyVehicleVM;
import io.swagger.annotations.ApiOperation;
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
 * gen by beetlsql 2020-04-27
 *
 * @author tiza
 */
@Slf4j
@RestController
@RequestMapping("controller/lock/apply")
public class LockApplyController {

    @Autowired
    private LockApplyService lockApplyService;

    @Autowired
    private LockApplyVinService vinService;

    @GetMapping("/self")
    @ApiOperation(value = "查询申请列表",notes ="申请人审批锁车列表" )
    @SuppressWarnings("unchecked")
    public ResponseEntity<List<LockApply>> self(LockApplyQuery query) {
        query.setApplyUser(BaseContextHandler.getLoginName());
        PageQuery pageQuery = lockApplyService.findAll(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    @GetMapping("/all")
    @ApiOperation(value = "查询审批状态列表",notes ="审批状态列表" )
    @SuppressWarnings("unchecked")
    public ResponseEntity<List<LockApply>> all(LockApplyQuery query) {
        PageQuery pageQuery = lockApplyService.findAll(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    @GetMapping("/byInstance/{instanceId}")
    public ResponseEntity<LockApply> get(@PathVariable String instanceId) {
        return ResponseEntity.ok(lockApplyService.getLockApply(instanceId));
    }

    @GetMapping("vehicles")
    @ApiOperation(value = "根据申请单号查询锁车列表",notes ="某个申请单下的锁车列表" )
    @SuppressWarnings("unchecked")
    public ResponseEntity<List<LockApplyVehicleVM>> listApplyVehicle(LockApplyQuery query) {
        PageQuery pageQuery = vinService.pageQuery(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<LockApply> get(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(lockApplyService.get(id));
    }

    @PostMapping
    @ApiOperation(value = "申请锁车",notes ="申请锁车" )
    public ResponseEntity create(@RequestBody @Valid LockApplyDto dto) {
        //todo判断是否管理员，管理员不允许申请
        LockApply newObj = lockApplyService.create(dto);
        return ResponseEntity.ok(newObj);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "修改申请单",notes ="修改申请单" )
    public ResponseEntity update(@PathVariable Long id, @RequestBody @Valid LockApplyDto dto) {
        Optional<LockApply> updatedObj = lockApplyService.update(id, dto);
        return ResponseUtil.wrapOrNotFound(updatedObj);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        lockApplyService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam Long[] ids) {
        lockApplyService.delete(ids);
        return ResponseEntity.ok().build();
    }
}
