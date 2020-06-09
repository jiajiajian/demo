package cn.com.tiza.service.mapper;


import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.domain.VehicleModel;
import cn.com.tiza.service.dto.VehicleModelDto;
import cn.com.tiza.web.rest.vm.VehicleModelVM;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Component
public class VehicleModelMapper {

    public VehicleModel dtoToEntity(VehicleModelDto dto) {
        if (dto == null) {
            return null;
        }
        VehicleModel entity = new VehicleModel();
        entity.setName(dto.getName());
        entity.setTonnage(dto.getTonnage());
        entity.setOrganizationId(dto.getOrganizationId());
        entity.setCreateTime(System.currentTimeMillis());
        entity.setCreateUserAccount(BaseContextHandler.getLoginName());
        entity.setCreateUserRealname(BaseContextHandler.getName());
        entity.setDescription(dto.getDescription());
        entity.setUpdateTime(System.currentTimeMillis());
        entity.setUpdateUserAccount(BaseContextHandler.getLoginName());
        entity.setUpdateUserRealname(BaseContextHandler.getName());
        entity.setVehicleTypeId(dto.getVehicleTypeId());
        return entity;
    }

    public VehicleModelVM toVM(VehicleModel entity) {
        if (entity == null) {
            return null;
        }
        VehicleModelVM vm = new VehicleModelVM();
        vm.setId(entity.getId());
        vm.setName(entity.getName());
        vm.setTonnage(entity.getTonnage());
        vm.setOrganizationId(entity.getOrganizationId());
        vm.setVehicleTypeId(entity.getVehicleTypeId());
        return vm;
    }

    public List<VehicleModelVM> entitiesToVMList(List<VehicleModel> entities) {
        return entities.stream()
                .map(this::toVM)
                .collect(Collectors.toList());
    }
}
