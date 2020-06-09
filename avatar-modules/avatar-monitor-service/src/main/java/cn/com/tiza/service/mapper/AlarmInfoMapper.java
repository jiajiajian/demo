package cn.com.tiza.service.mapper;

import cn.com.tiza.domain.AlarmHistory;
import cn.com.tiza.domain.AlarmInfo;
import cn.com.tiza.service.VehicleLocationService;
import cn.com.tiza.service.dto.AlarmStartDto;
import cn.com.tiza.service.dto.FaultStartDto;
import cn.com.tiza.service.dto.FenceStartDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author tz0920
 */
@Component
public class AlarmInfoMapper {
    @Autowired
    private VehicleLocationService locationService;

    public AlarmInfo alarmDtoToEntity(AlarmStartDto dto) {
        if (dto == null) {
            return null;
        }
        AlarmInfo entity = new AlarmInfo();
        entity.setAlarmType(dto.getAlarmType());
        entity.setVin(dto.getVin());
        entity.setBeginTime(dto.getBeginTime());
        entity.setAlarmCode(dto.getAlarmItem());
        entity.setLat(dto.getLat());
        entity.setLon(dto.getLon());
        entity.setAddress(locationService.getLocationByLonAndLat(dto.getLon(), dto.getLat()));
        entity.setOrganizationId(dto.getOrganizationId());
        entity.setProvince(dto.getProvince());
        entity.setCity(dto.getCity());
        entity.setArea(dto.getArea());
        return entity;
    }

    public AlarmInfo faultDtoToEntity(FaultStartDto dto) {
        if (dto == null) {
            return null;
        }
        AlarmInfo entity = new AlarmInfo();
        entity.setAlarmType(dto.getAlarmType());
        entity.setVin(dto.getVin());
        entity.setBeginTime(dto.getBeginTime());
        entity.setLat(dto.getLat());
        entity.setLon(dto.getLon());
        entity.setSpnFmi(dto.getSpnFmi());
        entity.setTla(dto.getTla());
        entity.setFrequency(dto.getFrequency());
        entity.setAddress(locationService.getLocationByLonAndLat(dto.getLon(), dto.getLat()));
        entity.setOrganizationId(dto.getOrganizationId());
        entity.setProvince(dto.getProvince());
        entity.setCity(dto.getCity());
        entity.setArea(dto.getArea());
        return entity;
    }

    public AlarmInfo fenceAlarmDtoToEntity(FenceStartDto dto) {
        if (dto == null) {
            return null;
        }
        AlarmInfo entity = new AlarmInfo();
        entity.setAlarmType(dto.getAlarmType());
        entity.setVin(dto.getVin());
        entity.setBeginTime(dto.getBeginTime());
        entity.setLat(dto.getLat());
        entity.setLon(dto.getLon());
        entity.setAddress(locationService.getLocationByLonAndLat(dto.getLon(), dto.getLat()));
        entity.setOrganizationId(dto.getOrganizationId());
        entity.setFenceId(dto.getFenceId());
        entity.setOrgType(dto.getOrgType());
        entity.setProvince(dto.getProvince());
        entity.setCity(dto.getCity());
        entity.setArea(dto.getArea());
        return entity;
    }

    public AlarmHistory infoToHistory(AlarmInfo info) {
        AlarmHistory alarmHistory = new AlarmHistory();
        alarmHistory.setId(info.getId());
        alarmHistory.setVin(info.getVin());
        alarmHistory.setOrgType(info.getOrgType());
        alarmHistory.setOrganizationId(info.getOrganizationId());
        alarmHistory.setAlarmType(info.getAlarmType());
        alarmHistory.setAlarmState(0);
        alarmHistory.setBeginTime(info.getBeginTime());
        alarmHistory.setFenceId(info.getFenceId());
        alarmHistory.setAlarmCode(info.getAlarmCode());
        alarmHistory.setSpnFmi(info.getSpnFmi());
        alarmHistory.setTla(info.getTla());
        alarmHistory.setFrequency(info.getFrequency());
        alarmHistory.setProvince(info.getProvince());
        alarmHistory.setCity(info.getCity());
        alarmHistory.setArea(info.getArea());
        alarmHistory.setLat(info.getLat());
        alarmHistory.setLon(info.getLon());
        alarmHistory.setAddress(info.getAddress());
        return alarmHistory;
    }
}
