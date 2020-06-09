package cn.com.tiza.service.mapper;


import cn.com.tiza.domain.FaultDictItem;
import cn.com.tiza.service.dto.FaultDictItemDto;
import cn.com.tiza.web.rest.vm.FaultDictItemVM;
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
public class FaultDictItemMapper {

    public FaultDictItem dtoToEntity(FaultDictItemDto dto) {
        if (dto == null) {
            return null;
        }
        FaultDictItem entity = new FaultDictItem();
        entity.setFmi(dto.getFmi());
        entity.setSpn(dto.getSpn());
        entity.setFmiName(dto.getFmiName());
        entity.setSpnName(dto.getSpnName());
        entity.setOrganizationId(dto.getOrganizationId());
        entity.setTlaId(dto.getTlaId());
        return entity;
    }

    public FaultDictItemVM toVM(FaultDictItem entity) {
        if (entity == null) {
            return null;
        }
        FaultDictItemVM vm = new FaultDictItemVM();
        vm.setId(entity.getId());
      //  vm.setFmi(entity.getFmi());
       // vm.setSpn(entity.getSpn());
        vm.setSpnName(entity.getSpnName());
        vm.setFmiName(entity.getFmiName());
        return vm;
    }

    public List<FaultDictItemVM> entitiesToVMList(List<FaultDictItem> entities) {
        return entities.stream()
                .map(this::toVM)
                .collect(Collectors.toList());
    }
}
