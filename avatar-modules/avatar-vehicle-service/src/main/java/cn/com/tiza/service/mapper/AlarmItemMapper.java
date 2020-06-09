package cn.com.tiza.service.mapper;


import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dao.AlarmItemDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.tiza.domain.AlarmItem;
import cn.com.tiza.service.dto.AlarmItemDto;
import cn.com.tiza.web.rest.vm.AlarmItemVM;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Mapper
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Component
public class AlarmItemMapper {
    @Autowired
    private AlarmItemDao alarmItemDao;

    public AlarmItem dtoToEntity(AlarmItemDto dto) {
        if (dto == null) {
            return null;
        }
        AlarmItem entity = new AlarmItem();
        entity.setAlarmItem(StringUtils.join(dto.getItems(), ","));
        entity.setOrganizationId(dto.getOrganizationId());
        entity.setCreateTime(System.currentTimeMillis());
        entity.setCreateUserAccount(BaseContextHandler.getLoginName());
        entity.setCreateUserRealname(BaseContextHandler.getName());
        entity.setUpdateTime(System.currentTimeMillis());
        entity.setUpdateUserAccount(BaseContextHandler.getLoginName());
        entity.setUpdateUserRealname(BaseContextHandler.getName());
        return entity;
    }

    public AlarmItemVM toVM(AlarmItem entity) {
        if (entity == null) {
            return null;
        }
        AlarmItemVM vm = new AlarmItemVM();
        vm.setId(entity.getId());
        vm.setAlarmItem(entity.getAlarmItem());
        vm.setAlarmItemList(Arrays.asList(entity.getAlarmItem().split(",")));
        vm.setOrganizationId(entity.getOrganizationId());
        return vm;
    }

    public List<AlarmItemVM> entitiesToVMList(List<AlarmItem> entities) {
        return entities.stream()
                .map(this::toVM)
                .collect(Collectors.toList());
    }
}
