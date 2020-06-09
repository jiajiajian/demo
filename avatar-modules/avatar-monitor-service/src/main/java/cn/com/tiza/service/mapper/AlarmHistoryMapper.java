package cn.com.tiza.service.mapper;


import org.springframework.stereotype.Component;

import cn.com.tiza.domain.AlarmHistory;
import cn.com.tiza.service.dto.AlarmHistoryDto;
import cn.com.tiza.web.rest.vm.AlarmHistoryVM;

import java.util.List;
import java.util.stream.Collectors;

/**
*  Mapper
* gen by beetlsql 2020-03-31
* @author tiza
*/
@Component
public class AlarmHistoryMapper {

    public AlarmHistory dtoToEntity(AlarmHistoryDto dto) {
        if(dto == null) {
            return null;
        }
        AlarmHistory entity = new AlarmHistory();
        entity.setId(dto.getId());
        entity.setOrgType(dto.getOrgType());
        entity.setAddress(dto.getAddress());
        entity.setAlarmCode(dto.getAlarmCode());
        entity.setArea(dto.getArea());
        entity.setBeginTime(dto.getBeginTime());
        entity.setCity(dto.getCity());
        entity.setDuration(dto.getDuration());
        entity.setEndTime(dto.getEndTime());
        entity.setFaultParameter(dto.getFaultParameter());
        entity.setFenceId(dto.getFenceId());
        entity.setLat(dto.getLat());
        entity.setLon(dto.getLon());
        entity.setOrganizationId(dto.getOrganizationId());
        entity.setProvince(dto.getProvince());
        entity.setSpnFmi(dto.getSpn());
        entity.setVin(dto.getVin());
        return entity;
    }

    public AlarmHistoryVM toVM(AlarmHistory entity) {
        if(entity == null) {
            return null;
        }
        AlarmHistoryVM vm = new AlarmHistoryVM();
        vm.setId(entity.getId());
        vm.setAddress(entity.getAddress());
        vm.setBeginTime(entity.getBeginTime());
        vm.setDuration(entity.getDuration());
        vm.setEndTime(entity.getEndTime());
        vm.setVin(entity.getVin());
        return vm;
    }

    public List<AlarmHistoryVM> entitiesToVMList(List<AlarmHistory> entities) {
        return entities.stream()
                .map(this::toVM)
                .collect(Collectors.toList());
    }
}
