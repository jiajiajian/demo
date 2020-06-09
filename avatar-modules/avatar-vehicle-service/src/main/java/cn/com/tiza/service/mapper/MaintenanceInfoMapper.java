package cn.com.tiza.service.mapper;


import org.springframework.stereotype.Component;

import cn.com.tiza.domain.MaintenanceInfo;
import cn.com.tiza.service.dto.MaintenanceInfoDto;
import cn.com.tiza.web.rest.vm.MaintenanceInfoVM;

import java.util.List;
import java.util.stream.Collectors;

/**
*  Mapper
* gen by beetlsql 2020-03-31
* @author tiza
*/
@Component
public class MaintenanceInfoMapper {

    public MaintenanceInfo dtoToEntity(MaintenanceInfoDto dto) {
        if(dto == null) {
            return null;
        }
        MaintenanceInfo entity = new MaintenanceInfo();
        entity.setId(dto.getId());
        entity.setHours(dto.getHours());
        entity.setMaintenanceType(dto.getMaintenanceType());
        entity.setCreateTime(dto.getCreateTime());
        entity.setCreateUserAccount(dto.getCreateUserAccount());
        entity.setCreateUserRealname(dto.getCreateUserRealname());
        entity.setMaintenanceContent(dto.getMaintenanceContent());
        entity.setRemark(dto.getRemark());
        return entity;
    }

    public MaintenanceInfoVM toVM(MaintenanceInfo entity) {
        if(entity == null) {
            return null;
        }
        MaintenanceInfoVM vm = new MaintenanceInfoVM();
        vm.setId(entity.getId());
        vm.setHours(entity.getHours());
        vm.setMaintenanceType(entity.getMaintenanceType());
        vm.setCreateTime(entity.getCreateTime());
        vm.setCreateUserAccount(entity.getCreateUserAccount());
        vm.setCreateUserRealname(entity.getCreateUserRealname());
        vm.setMaintenanceContent(entity.getMaintenanceContent());
        vm.setRemark(entity.getRemark());
        return vm;
    }

    public List<MaintenanceInfoVM> entitiesToVMList(List<MaintenanceInfo> entities) {
        return entities.stream()
                .map(this::toVM)
                .collect(Collectors.toList());
    }
}
