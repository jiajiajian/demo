package cn.com.tiza.service.mapper;


import org.springframework.stereotype.Component;

import cn.com.tiza.domain.FenceVehicle;
import cn.com.tiza.service.dto.FenceVehicleDto;
import cn.com.tiza.web.rest.vm.FenceVehicleVM;

import java.util.List;
import java.util.stream.Collectors;

/**
*  Mapper
* gen by beetlsql 2020-03-31
* @author tiza
*/
@Component
public class FenceVehicleMapper {

    public FenceVehicle dtoToEntity(FenceVehicleDto dto) {
        if(dto == null) {
            return null;
        }
        FenceVehicle entity = new FenceVehicle();
        entity.setId(dto.getId());
        entity.setFenceId(dto.getFenceId());
        entity.setVin(dto.getVin());
        return entity;
    }

    public FenceVehicleVM toVM(FenceVehicle entity) {
        if(entity == null) {
            return null;
        }
        FenceVehicleVM vm = new FenceVehicleVM();
        vm.setId(entity.getId());
        vm.setFenceId(entity.getFenceId());
        vm.setVin(entity.getVin());
        return vm;
    }

    public List<FenceVehicleVM> entitiesToVMList(List<FenceVehicle> entities) {
        return entities.stream()
                .map(this::toVM)
                .collect(Collectors.toList());
    }
}
