package cn.com.tiza.service.mapper;


import cn.com.tiza.domain.VehicleRealtime;
import cn.com.tiza.service.dto.VehicleRealtimeDto;
import cn.com.tiza.web.rest.vm.VehicleRealtimeVM;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Mapper
 * gen by beetlsql 2020-04-14
 *
 * @author tiza
 */
@Component
public class VehicleRealtimeMapper {

    public VehicleRealtime dtoToEntity(VehicleRealtimeDto dto) {
        if (dto == null) {
            return null;
        }
        VehicleRealtime entity = new VehicleRealtime();
        entity.setId(dto.getId());
        entity.setAcc(dto.getAcc());
        entity.setFaultStatus(dto.getFaultStatus());
        entity.setGps(dto.getGps());
        entity.setLock(dto.getLock());
        entity.setOneLevelLock(dto.getOneLevelLock());
        entity.setThreeLevelLock(dto.getThreeLevelLock());
        entity.setTwoLevelLock(dto.getTwoLevelLock());
        entity.setDataUpdateTime(dto.getDataUpdateTime());
        entity.setDebugEndTime(dto.getDebugEndTime());
        entity.setDebugStartTime(dto.getDebugStartTime());
        entity.setDescription(dto.getDescription());
        entity.setGpsAddress(dto.getGpsAddress());
        entity.setGpsArea(dto.getGpsArea());
        entity.setGpsCity(dto.getGpsCity());
        entity.setGpsProvince(dto.getGpsProvince());
        entity.setGpsTime(dto.getGpsTime());
        entity.setLastLat(dto.getLastLat());
        entity.setLastLon(dto.getLastLon());
        entity.setLat(dto.getLat());
        entity.setLon(dto.getLon());
        entity.setTotalWorkTime(dto.getTotalWorkTime());
        entity.setVin(dto.getVin());
        return entity;
    }

    public VehicleRealtimeVM toVM(VehicleRealtime entity) {
        if (entity == null) {
            return null;
        }
        VehicleRealtimeVM vm = new VehicleRealtimeVM();
        vm.setId(entity.getId());
        vm.setAcc(entity.getAcc());
        vm.setFaultStatus(entity.getFaultStatus());
        vm.setGps(entity.getGps());
        vm.setLock(entity.getLock());
        vm.setOneLevelLock(entity.getOneLevelLock());
        vm.setThreeLevelLock(entity.getThreeLevelLock());
        vm.setTwoLevelLock(entity.getTwoLevelLock());
        vm.setDataUpdateTime(entity.getDataUpdateTime());
        vm.setDebugEndTime(entity.getDebugEndTime());
        vm.setDebugStartTime(entity.getDebugStartTime());
        vm.setDescription(entity.getDescription());
        vm.setGpsAddress(entity.getGpsAddress());
        vm.setGpsArea(entity.getGpsArea());
        vm.setGpsCity(entity.getGpsCity());
        vm.setGpsProvince(entity.getGpsProvince());
        vm.setGpsTime(entity.getGpsTime());
        vm.setLastLat(entity.getLastLat());
        vm.setLastLon(entity.getLastLon());
        vm.setLat(entity.getLat());
        vm.setLon(entity.getLon());
        vm.setTotalWorkTime(entity.getTotalWorkTime());
        vm.setVin(entity.getVin());
        vm.setSignalStrength(entity.getSignalStrength());
        if (Objects.isNull(entity.getDataUpdateTime())) {
            vm.setOnlineStatus(0);
        } else {
            if ((System.currentTimeMillis() - entity.getDataUpdateTime()) > 300000) {
                vm.setOnlineStatus(0);
            } else {
                vm.setOnlineStatus(1);
            }
        }
        return vm;
    }

    public List<VehicleRealtimeVM> entitiesToVMList(List<VehicleRealtime> entities) {
        return entities.stream()
                .map(this::toVM)
                .collect(Collectors.toList());
    }
}
