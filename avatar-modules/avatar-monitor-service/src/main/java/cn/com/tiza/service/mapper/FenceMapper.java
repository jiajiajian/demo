package cn.com.tiza.service.mapper;


import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.domain.Fence;
import cn.com.tiza.dto.UserType;
import cn.com.tiza.service.dto.FenceDto;
import cn.com.tiza.web.rest.vm.FenceVM;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper
 * gen by beetlsql 2020-03-31
 *
 * @author tiza
 */
@Component
public class FenceMapper {

    public Fence dtoToEntity(FenceDto dto) {
        if (dto == null) {
            return null;
        }
        Fence entity = new Fence();
        entity.setAlarmType(dto.getAlarmType());
        entity.setFenceType(dto.getFenceType());
        entity.setVehicleNum(dto.getVehicleNum());
        entity.setArea(dto.getArea());
        entity.setCreateTime(System.currentTimeMillis());
        entity.setCreateUserAccount(BaseContextHandler.getLoginName());
        entity.setCreateUserRealname(BaseContextHandler.getName());
        entity.setName(dto.getName());

        if (BaseContextHandler.getUserType().equals(UserType.ORGANIZATION.name())) {
            entity.setOrganizationId(BaseContextHandler.getOrgId());
            entity.setOrgType(1);
        } else {
            entity.setOrganizationId(BaseContextHandler.getFinanceId());
            entity.setOrgType(2);
        }
        entity.setUpdateTime(System.currentTimeMillis());
        entity.setUpdateUserAccount(BaseContextHandler.getLoginName());
        entity.setUpdateUserRealname(BaseContextHandler.getName());
        return entity;
    }

    public FenceVM toVM(Fence entity) {
        if (entity == null) {
            return null;
        }
        FenceVM vm = new FenceVM();
        vm.setId(entity.getId());
        vm.setAlarmType(entity.getAlarmType());
        vm.setFenceType(entity.getFenceType());
        vm.setVehicleNum(entity.getVehicleNum());
        vm.setArea(entity.getArea());
        vm.setName(entity.getName());
        vm.setOrganizationId(entity.getOrganizationId());
        vm.setOrgType(entity.getOrgType());
        vm.setUpdateTime(entity.getUpdateTime());
        vm.setUpdateUserAccount(entity.getUpdateUserAccount());
        vm.setUpdateUserRealname(entity.getUpdateUserRealname());
        return vm;
    }

    public List<FenceVM> entitiesToVMList(List<Fence> entities) {
        return entities.stream()
                .map(this::toVM)
                .collect(Collectors.toList());
    }
}
