package cn.com.tiza.service.mapper;


import cn.com.tiza.domain.Simcard;
import cn.com.tiza.service.dto.SimcardDto;
import cn.com.tiza.web.vm.SimcardVM;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper
 * gen by beetlsql 2020-03-09
 *
 * @author tiza
 */
@Component
public class SimcardMapper {

    public Simcard dtoToEntity(SimcardDto dto) {
        if (dto == null) {
            return null;
        }
        Simcard entity = new Simcard();
        entity.setCardWayId(dto.getCardWayId());
        entity.setStatus(0);
        entity.setCardOwner(dto.getCardOwner());
        entity.setCode(dto.getCode());
        entity.setCreateTime(System.currentTimeMillis());
        entity.setOrderNo(dto.getOrderNo());
        entity.setDepartment(dto.getDepartment());
        entity.setOperator(dto.getOperator());
        return entity;
    }

    public SimcardVM toVM(Simcard entity) {
        if (entity == null) {
            return null;
        }
        SimcardVM vm = new SimcardVM();
        vm.setId(entity.getId());
        vm.setCardWayId(entity.getCardWayId());
        vm.setStatus(entity.getStatus());
        vm.setCardOwner(entity.getCardOwner());
        vm.setCode(entity.getCode());
        vm.setCreateTime(entity.getCreateTime());
        vm.setOrderNo(entity.getOrderNo());
        vm.setDepartment(entity.getDepartment());
        vm.setOperator(entity.getOperator());
        return vm;
    }

    public List<SimcardVM> entitiesToVMList(List<Simcard> entities) {
        return entities.stream()
                .map(this::toVM)
                .collect(Collectors.toList());
    }

}
