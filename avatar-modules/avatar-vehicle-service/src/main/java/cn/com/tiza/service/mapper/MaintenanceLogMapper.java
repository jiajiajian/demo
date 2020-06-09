package cn.com.tiza.service.mapper;


import org.springframework.stereotype.Component;

import cn.com.tiza.domain.MaintenanceLog;
import cn.com.tiza.service.dto.MaintenanceLogDto;
import cn.com.tiza.web.rest.vm.MaintenanceLogVM;

import java.util.List;
import java.util.stream.Collectors;

/**
*  Mapper
* gen by beetlsql 2020-03-31
* @author tiza
*/
@Component
public class MaintenanceLogMapper {

    public MaintenanceLog dtoToEntity(MaintenanceLogDto dto) {
        if(dto == null) {
            return null;
        }
        MaintenanceLog entity = new MaintenanceLog();
        entity.setId(dto.getId());
        entity.setHandleStatus(dto.getHandleStatus());
        entity.setRemindHours(dto.getRemindHours());
        entity.setHandleResult(dto.getHandleResult());
        entity.setHandleTime(dto.getHandleTime());
        entity.setHandleUserAccount(dto.getHandleUserAccount());
        entity.setHandleUserRealname(dto.getHandleUserRealname());
        entity.setRemindTime(dto.getRemindTime());
        entity.setVin(dto.getVin());
        return entity;
    }

    public MaintenanceLogVM toVM(MaintenanceLog entity) {
        if(entity == null) {
            return null;
        }
        MaintenanceLogVM vm = new MaintenanceLogVM();
        vm.setId(entity.getId());
        vm.setHandleStatus(entity.getHandleStatus());
        vm.setRemindHours(entity.getRemindHours());
        vm.setHandleResult(entity.getHandleResult());
        vm.setHandleTime(entity.getHandleTime());
        vm.setHandleUserAccount(entity.getHandleUserAccount());
        vm.setHandleUserRealname(entity.getHandleUserRealname());
        vm.setRemindTime(entity.getRemindTime());
        vm.setVin(entity.getVin());
        return vm;
    }

    public List<MaintenanceLogVM> entitiesToVMList(List<MaintenanceLog> entities) {
        return entities.stream()
                .map(this::toVM)
                .collect(Collectors.toList());
    }
}
