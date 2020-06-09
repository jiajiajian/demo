package cn.com.tiza.service.mapper;


import org.springframework.stereotype.Component;

import cn.com.tiza.domain.VehicleDebug;
import cn.com.tiza.service.dto.VehicleDebugDto;
import cn.com.tiza.web.rest.vm.VehicleDebugVM;

import java.util.List;
import java.util.stream.Collectors;

/**
*  Mapper
* gen by beetlsql 2020-03-17
* @author tiza
*/
@Component
public class VehicleDebugMapper {

    public VehicleDebug dtoToEntity(VehicleDebugDto dto) {
        if(dto == null) {
            return null;
        }
        VehicleDebug entity = new VehicleDebug();
        entity.setId(dto.getId());
        entity.setStatus(dto.getStatus());
        entity.setCreateTime(dto.getCreateTime());
        entity.setCreateUserAccount(dto.getCreateUserAccount());
        entity.setCreateUserRealname(dto.getCreateUserRealname());
        entity.setDebugBeginTime(dto.getDebugBeginTime());
        entity.setDebugEndTime(dto.getDebugEndTime());
        entity.setDebugUserId(dto.getDebugUserId());
        entity.setUpdateTime(dto.getUpdateTime());
        entity.setUpdateUserAccount(dto.getUpdateUserAccount());
        entity.setUpdateUserRealname(dto.getUpdateUserRealname());
        entity.setVin(dto.getVin());
        return entity;
    }

    public VehicleDebugVM toVM(VehicleDebug entity) {
        if(entity == null) {
            return null;
        }
        VehicleDebugVM vm = new VehicleDebugVM();
        vm.setId(entity.getId());
        vm.setStatus(entity.getStatus());
        vm.setVin(entity.getVin());
        return vm;
    }

    public List<VehicleDebugVM> entitiesToVMList(List<VehicleDebug> entities) {
        return entities.stream()
                .map(this::toVM)
                .collect(Collectors.toList());
    }
}
