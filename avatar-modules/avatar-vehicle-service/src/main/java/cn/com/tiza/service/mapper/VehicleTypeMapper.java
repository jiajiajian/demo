package cn.com.tiza.service.mapper;


import cn.com.tiza.context.BaseContextHandler;
import org.springframework.stereotype.Component;

import cn.com.tiza.domain.VehicleType;
import cn.com.tiza.service.dto.VehicleTypeDto;
import cn.com.tiza.web.rest.vm.VehicleTypeVM;

import java.util.List;
import java.util.stream.Collectors;

/**
*  Mapper
* gen by beetlsql 2020-03-10
* @author tiza
*/
@Component
public class VehicleTypeMapper {

    public VehicleType dtoToEntity(VehicleTypeDto dto) {
        if(dto == null) {
            return null;
        }
        VehicleType entity = new VehicleType();
        entity.setCreateTime(System.currentTimeMillis());
        entity.setCreateUserAccount(BaseContextHandler.getLoginName());
        entity.setCreateUserRealname(BaseContextHandler.getLoginName());
        entity.setUpdateTime(System.currentTimeMillis());
        entity.setUpdateUserAccount(BaseContextHandler.getLoginName());
        entity.setUpdateUserRealname(BaseContextHandler.getLoginName());
        entity.setDescription(dto.getDescription());
        entity.setName(dto.getName());
        entity.setOrganizationId(dto.getOrganizationId());
        return entity;
    }

    public VehicleTypeVM toVM(VehicleType entity) {
        if(entity == null) {
            return null;
        }
        VehicleTypeVM vm = new VehicleTypeVM();
        vm.setId(entity.getId());
        vm.setName(entity.getName());
        vm.setOrganizationId(entity.getOrganizationId());
        return vm;
    }

    public List<VehicleTypeVM> entitiesToVMList(List<VehicleType> entities) {
        return entities.stream()
                .map(this::toVM)
                .collect(Collectors.toList());
    }
}
