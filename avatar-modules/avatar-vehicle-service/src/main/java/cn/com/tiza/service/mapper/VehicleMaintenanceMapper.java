package cn.com.tiza.service.mapper;


import cn.com.tiza.context.BaseContextHandler;
import org.springframework.stereotype.Component;

import cn.com.tiza.domain.VehicleMaintenance;
import cn.com.tiza.service.dto.VehicleMaintenanceDto;
import cn.com.tiza.web.rest.vm.VehicleMaintenanceVM;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
*  Mapper
* gen by beetlsql 2020-03-31
* @author tiza
*/
@Component
public class VehicleMaintenanceMapper {

    public VehicleMaintenance dtoToEntity(VehicleMaintenanceDto dto) {
        if(dto == null) {
            return null;
        }
        VehicleMaintenance entity = new VehicleMaintenance();
        entity.setId(dto.getId());
        entity.setCreateTime(dto.getCreateTime());
        entity.setCreateUserAccount(dto.getCreateUserAccount());
        entity.setCreateUserRealname(dto.getCreateUserRealname());
        entity.setDescription(dto.getDescription());
        entity.setItemDetail(dto.getItemDetail());
        entity.setItemName(dto.getItemName());
        entity.setOrganizationId(dto.getOrganizationId());
        entity.setUpdateTime(dto.getUpdateTime());
        entity.setUpdateUserAccount(dto.getUpdateUserAccount());
        entity.setUpdateUserRealname(dto.getUpdateUserRealname());
        return entity;
    }

    public VehicleMaintenanceVM toVM(VehicleMaintenance entity) {
        if(entity == null) {
            return null;
        }
        String []content=entity.getItemDetail().split(",");
        VehicleMaintenanceVM vm = new VehicleMaintenanceVM();
        vm.setId(entity.getId());
        vm.setCreateTime(entity.getCreateTime());
        vm.setCreateUserAccount(entity.getCreateUserAccount());
        vm.setCreateUserRealname(entity.getCreateUserRealname());
        vm.setDescription(entity.getDescription());
        vm.setItemDetail(entity.getItemDetail());
        vm.setItemName(entity.getItemName());
        vm.setContent(Arrays.asList(content));
        vm.setOrganizationId(entity.getOrganizationId());
        vm.setUpdateTime(entity.getUpdateTime());
        vm.setUpdateUserAccount(entity.getUpdateUserAccount());
        vm.setUpdateUserRealname(entity.getUpdateUserRealname());
        return vm;
    }

    public List<VehicleMaintenanceVM> entitiesToVMList(List<VehicleMaintenance> entities) {
        return entities.stream()
                .map(this::toVM)
                .collect(Collectors.toList());
    }
}
