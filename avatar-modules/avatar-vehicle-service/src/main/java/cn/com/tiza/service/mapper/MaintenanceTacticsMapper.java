package cn.com.tiza.service.mapper;


import org.springframework.stereotype.Component;

import cn.com.tiza.domain.MaintenanceTactics;
import cn.com.tiza.service.dto.MaintenanceTacticsDto;
import cn.com.tiza.web.rest.vm.MaintenanceTacticsVM;

import java.util.List;
import java.util.stream.Collectors;

/**
*  Mapper
* gen by beetlsql 2020-03-31
* @author tiza
*/
@Component
public class MaintenanceTacticsMapper {

    public MaintenanceTactics dtoToEntity(MaintenanceTacticsDto dto) {
        if(dto == null) {
            return null;
        }
        MaintenanceTactics entity = new MaintenanceTactics();
        entity.setId(dto.getId());
        entity.setCreateTime(dto.getCreateTime());
        entity.setCreateUserAccount(dto.getCreateUserAccount());
        entity.setCreateUserRealname(dto.getCreateUserRealname());
        entity.setRemark(dto.getRemark());
        entity.setTacticsName(dto.getTacticsName());
        entity.setUpdateTime(dto.getUpdateTime());
        entity.setUpdateUserAccount(dto.getUpdateUserAccount());
        entity.setUpdateUserRealname(dto.getUpdateUserRealname());
        return entity;
    }

    public MaintenanceTacticsVM toVM(MaintenanceTactics entity) {
        if(entity == null) {
            return null;
        }
        MaintenanceTacticsVM vm = new MaintenanceTacticsVM();
        vm.setId(entity.getId());
        vm.setCreateTime(entity.getCreateTime());
        vm.setCreateUserAccount(entity.getCreateUserAccount());
        vm.setCreateUserRealname(entity.getCreateUserRealname());
        vm.setOrgName(entity.getOrgName());
        vm.setRemark(entity.getRemark());
        vm.setTacticsName(entity.getTacticsName());
        vm.setUpdateTime(entity.getUpdateTime());
        vm.setUpdateUserAccount(entity.getUpdateUserAccount());
        vm.setUpdateUserRealname(entity.getUpdateUserRealname());
        vm.setBindNames(entity.getBindNames());
        return vm;
    }

    public List<MaintenanceTacticsVM> entitiesToVMList(List<MaintenanceTactics> entities) {
        return entities.stream()
                .map(this::toVM)
                .collect(Collectors.toList());
    }
}
