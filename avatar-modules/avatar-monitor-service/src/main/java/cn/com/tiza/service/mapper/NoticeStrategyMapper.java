package cn.com.tiza.service.mapper;


import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dao.NoticeStrategyDao;
import cn.com.tiza.domain.NoticeStrategy;
import cn.com.tiza.dto.UserType;
import cn.com.tiza.service.dto.NoticeMethod;
import cn.com.tiza.service.dto.NoticeStrategyDto;
import cn.com.tiza.web.rest.vm.NoticeMethodOption;
import cn.com.tiza.web.rest.vm.NoticeStrategyVM;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Mapper
 * gen by beetlsql 2020-03-23
 *
 * @author tiza
 */
@Component
public class NoticeStrategyMapper {

    @Autowired
    private NoticeStrategyDao noticeStrategyDao;

    public NoticeStrategy dtoToEntity(NoticeStrategyDto dto) {
        if (dto == null) {
            return null;
        }
        NoticeStrategy entity = new NoticeStrategy();
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        Optional.ofNullable(dto.getRemindWay())
                .ifPresent(method -> entity.setRemindWay(String.join(",", method
                        .toArray(new String[method.size()]))));
        Optional.ofNullable(dto.getRoleIds())
                .ifPresent(roleIds -> entity.setRoleIds(StringUtils.join(dto.getRoleIds(), ",")));
        Optional.ofNullable(dto.getUserIds())
                .ifPresent(roleIds -> entity.setUserIds(StringUtils.join(dto.getUserIds(), ",")));
        entity.setDescription(dto.getDescription());
        if (BaseContextHandler.getUserType().equals(UserType.ORGANIZATION.name())) {
            entity.setOrgType(1);
            entity.setOrganizationId(BaseContextHandler.getOrgId());
        } else {
            entity.setOrgType(2);
            entity.setOrganizationId(BaseContextHandler.getFinanceId());
        }

        entity.setUpdateTime(System.currentTimeMillis());
        entity.setUpdateUserAccount(BaseContextHandler.getLoginName());
        entity.setUpdateUserRealname(BaseContextHandler.getName());

        return entity;
    }

    public NoticeStrategy copyProps(NoticeStrategyDto dto, NoticeStrategy entity) {
        if (null == entity) {
            return null;
        }
        if (Objects.nonNull(dto.getRemindWay()) && dto.getRemindWay().size() > 0) {
            String remindWay = String.join(",", dto.getRemindWay()
                    .toArray(new String[dto.getRemindWay().size()]));
            entity.setRemindWay(remindWay);
        } else {
            entity.setRemindWay(null);
        }

        if (Objects.nonNull(dto.getRoleIds()) && dto.getRoleIds().size() > 0) {
            String roleIds = StringUtils.join(dto.getRoleIds(), ",");
            entity.setRoleIds(roleIds);
        } else {
            entity.setRoleIds(null);
        }

        if (Objects.nonNull(dto.getUserIds()) && dto.getUserIds().size() > 0) {
            String userIds = StringUtils.join(dto.getUserIds(), ",");
            entity.setUserIds(userIds);
        } else {
            entity.setUserIds(null);
        }
        entity.setUpdateTime(System.currentTimeMillis());
        entity.setUpdateUserAccount(BaseContextHandler.getLoginName());
        entity.setUpdateUserRealname(BaseContextHandler.getName());
        return entity;
    }


    public NoticeStrategyVM toVM(NoticeStrategy entity) {
        NoticeStrategyVM vm = new NoticeStrategyVM();
        vm.setId(entity.getId());
        //获取用户list 和 角色list
        if (org.springframework.util.StringUtils.hasText(entity.getUserIds())) {
            List<Long> userIdList = Arrays.stream(entity.getUserIds().split(","))
                    .map(userId -> Long.parseLong(userId))
                    .collect(Collectors.toList());
            vm.setUserList(noticeStrategyDao.findUserListByIds(userIdList));
        }
        if (org.springframework.util.StringUtils.hasText(entity.getRoleIds())) {
            List<Long> roleIdList = Arrays.stream(entity.getRoleIds().split(","))
                    .map(roleId -> Long.parseLong(roleId))
                    .collect(Collectors.toList());
            vm.setRoleList(noticeStrategyDao.findRoleListByIds(roleIdList));
        }
        vm.setRemindWay(methodsToVMList(NoticeMethod.parse(entity.getRemindWay())));
        return vm;
    }

    public NoticeStrategyVM toRowVM(NoticeStrategy entity) {
        NoticeStrategyVM vm = new NoticeStrategyVM();
        vm.setCode(entity.getCode());
        vm.setName(entity.getName());
        vm.setId(entity.getId());
        vm.setRemindWay(methodsToVMList(NoticeMethod.parse(entity.getRemindWay())));
        vm.setRemindWayStr(vm.getRemindWay().stream().map(NoticeMethodOption::getLabel).collect(Collectors.joining(",")));
        return vm;
    }

    public List<NoticeStrategyVM> entitiesToVMList(List<NoticeStrategy> entities) {
        return entities.stream()
                .map(this::toRowVM)
                .collect(Collectors.toList());
    }

    public List<NoticeMethodOption> methodsToVMList(List<NoticeMethod> methods) {
        return methods.stream()
                .map(m -> new NoticeMethodOption(m))
                .collect(Collectors.toList());
    }
}
