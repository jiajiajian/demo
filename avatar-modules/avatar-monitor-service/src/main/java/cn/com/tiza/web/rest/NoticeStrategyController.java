package cn.com.tiza.web.rest;

import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dto.AlarmType;
import cn.com.tiza.dto.RoleType;
import cn.com.tiza.dto.SelectOption;
import cn.com.tiza.dto.UserType;
import cn.com.tiza.service.AlarmConsumeStrategyFactory;
import cn.com.tiza.service.AlarmService;
import cn.com.tiza.service.NoticeStrategyService;
import cn.com.tiza.service.dto.AlarmState;
import cn.com.tiza.service.dto.KafkaAlarmDto;
import cn.com.tiza.service.dto.NoticeStrategyDto;
import cn.com.tiza.service.mapper.NoticeStrategyMapper;
import cn.com.tiza.web.rest.util.ResponseUtil;
import cn.com.tiza.web.rest.vm.NoticeStrategyVM;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Controller
 * gen by beetlsql 2020-03-23
 *
 * @author tiza
 */
@Slf4j
@RestController
@RequestMapping("/noticeStrategy")
public class NoticeStrategyController {

    @Autowired
    private NoticeStrategyService noticeStrategyService;
    @Autowired
    private NoticeStrategyMapper mapper;

    /**
     *  融资机构和机构用户返回自己通知策略
     *
     * @return
     */
    @GetMapping()
    public ResponseEntity<List<NoticeStrategyVM>> strategies() {
        return ResponseEntity.ok(noticeStrategyService.strategies());
    }

    @GetMapping("{id}")
    public ResponseEntity<NoticeStrategyVM> get(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(noticeStrategyService.get(id)
                .map(obj -> mapper.toVM(obj)));
    }


    @PutMapping()
    public ResponseEntity update(@RequestBody @Valid NoticeStrategyDto dto) {
        noticeStrategyService.update(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        noticeStrategyService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<Void> delete(@RequestParam Long[] ids) {
        noticeStrategyService.delete(ids);
        return ResponseEntity.ok().build();
    }

    /**
     * 用户下拉选
     *
     * @return
     */
    @GetMapping("/userOptions")
    public ResponseEntity<List<SelectOption>> userOptions() {
        if (BaseContextHandler.getUserType().equals(UserType.ADMIN.name())) {
            return ResponseEntity.ok(Lists.newArrayList());
        }
        Integer userType;
        if (BaseContextHandler.getUserType().equals(UserType.ORGANIZATION.name())) {
            userType = UserType.ORGANIZATION.getValue();
        } else {
            userType = UserType.FINANCE.getValue();
        }
        return ResponseEntity.ok(noticeStrategyService.userOptions(userType, BaseContextHandler.getOrgId()));
    }

    /**
     * 角色下拉选
     *
     * @return
     */
    @GetMapping("/roleOptions")
    public ResponseEntity<List<SelectOption>> roleOptions() {
        if (BaseContextHandler.getUserType().equals(UserType.ADMIN.name())) {
            return ResponseEntity.ok(Lists.newArrayList());
        }
        Integer roleType;
        if (BaseContextHandler.getUserType().equals(UserType.ORGANIZATION.name())) {
            roleType = RoleType.ORGANIZATION.getValue();
        } else {
            roleType = RoleType.FINANCE.getValue();
        }
        return ResponseEntity.ok(noticeStrategyService.roleOptions(roleType, BaseContextHandler.getRootOrgId(),BaseContextHandler.getFinanceId()));
    }

}
