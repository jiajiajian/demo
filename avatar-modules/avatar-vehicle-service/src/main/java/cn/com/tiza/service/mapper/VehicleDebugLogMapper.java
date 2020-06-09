package cn.com.tiza.service.mapper;


import org.springframework.stereotype.Component;

import cn.com.tiza.domain.VehicleDebugLog;
import cn.com.tiza.service.dto.VehicleDebugLogDto;
import cn.com.tiza.web.rest.vm.VehicleDebugLogVM;

import java.util.List;
import java.util.stream.Collectors;

/**
*  Mapper
* gen by beetlsql 2020-03-17
* @author tiza
*/
@Component
public class VehicleDebugLogMapper {

    public VehicleDebugLog dtoToEntity(VehicleDebugLogDto dto) {
        if(dto == null) {
            return null;
        }
        VehicleDebugLog entity = new VehicleDebugLog();
        entity.setStatus(dto.getStatus());
        entity.setContent(dto.getContent());
        entity.setItemKey(dto.getItemKey());
        entity.setItemName(dto.getItemName());
        entity.setVin(dto.getVin());
        return entity;
    }

    public VehicleDebugLogVM toVM(VehicleDebugLog entity) {
        if(entity == null) {
            return null;
        }
        VehicleDebugLogVM vm = new VehicleDebugLogVM();
        vm.setId(entity.getId());
        vm.setStatus(entity.getStatus());
        vm.setContent(entity.getContent());
        vm.setCreateTime(entity.getCreateTime());
        vm.setCreateUserAccount(entity.getCreateUserAccount());
        vm.setCreateUserRealname(entity.getCreateUserRealname());
        vm.setDebugTime(entity.getDebugTime());
        vm.setDebugUserId(entity.getDebugUserId());
        vm.setItemKey(entity.getItemKey());
        vm.setItemName(entity.getItemName());
        vm.setVin(entity.getVin());
        return vm;
    }

    public List<VehicleDebugLogVM> entitiesToVMList(List<VehicleDebugLog> entities) {
        return entities.stream()
                .map(this::toVM)
                .collect(Collectors.toList());
    }
}
