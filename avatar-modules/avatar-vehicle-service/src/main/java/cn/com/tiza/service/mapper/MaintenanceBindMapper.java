package cn.com.tiza.service.mapper;


import org.springframework.stereotype.Component;

import cn.com.tiza.domain.MaintenanceBind;
import cn.com.tiza.service.dto.MaintenanceBindDto;
import cn.com.tiza.web.rest.vm.MaintenanceBindVM;

import java.util.List;
import java.util.stream.Collectors;

/**
*  Mapper
* gen by beetlsql 2020-04-01
* @author tiza
*/
@Component
public class MaintenanceBindMapper {

    public MaintenanceBind dtoToEntity(MaintenanceBindDto dto) {
        if(dto == null) {
            return null;
        }
        MaintenanceBind entity = new MaintenanceBind();
        entity.setId(dto.getId());
        entity.setCreateTime(dto.getCreateTime());
        entity.setCreateUserAccount(dto.getCreateUserAccount());
        entity.setCreateUserRealname(dto.getCreateUserRealname());
        entity.setMaintenanceTacticsId(dto.getMaintenanceTacticsId());
        entity.setVehicleModelId(dto.getVehicleModelId());
        entity.setVehicleTypeId(dto.getVehicleTypeId());
        return entity;
    }

    public MaintenanceBindVM toVM(MaintenanceBind entity) {
        if(entity == null) {
            return null;
        }
        MaintenanceBindVM vm = new MaintenanceBindVM();
        vm.setId(entity.getId());
        vm.setCreateTime(entity.getCreateTime());
        vm.setCreateUserAccount(entity.getCreateUserAccount());
        vm.setCreateUserRealname(entity.getCreateUserRealname());
        vm.setMaintenanceTacticsId(entity.getMaintenanceTacticsId());
        vm.setVehicleModelId(entity.getVehicleModelId());
        vm.setVehicleTypeId(entity.getVehicleTypeId());
        return vm;
    }

    public List<MaintenanceBindVM> entitiesToVMList(List<MaintenanceBind> entities) {
        return entities.stream()
                .map(this::toVM)
                .collect(Collectors.toList());
    }
}
