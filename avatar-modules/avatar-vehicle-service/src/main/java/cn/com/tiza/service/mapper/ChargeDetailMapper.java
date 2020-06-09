package cn.com.tiza.service.mapper;


import cn.com.tiza.domain.ChargeDetail;
import cn.com.tiza.service.dto.ChargeDetailDto;
import cn.com.tiza.web.rest.vm.ChargeDetailVM;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper
 * gen by beetlsql 2020-04-20
 *
 * @author tiza
 */
@Component
public class ChargeDetailMapper {

    public ChargeDetail dtoToEntity(ChargeDetailDto dto, Long chargeId) {
        if (dto == null) {
            return null;
        }
        ChargeDetail entity = new ChargeDetail();
        entity.setBegin(dto.getBegin());
        entity.setEnd(dto.getEnd());
        entity.setChargeId(chargeId);
        entity.setFee(dto.getFee());
        return entity;
    }

    public ChargeDetailVM toVM(ChargeDetail entity) {
        if (entity == null) {
            return null;
        }
        ChargeDetailVM vm = new ChargeDetailVM();
        vm.setId(entity.getId());
        vm.setBegin(entity.getBegin());
        vm.setEnd(entity.getEnd());
        vm.setChargeId(entity.getChargeId());
        vm.setFee(entity.getFee());
        return vm;
    }

    public List<ChargeDetailVM> entitiesToVMList(List<ChargeDetail> entities) {
        return entities.stream()
                .map(this::toVM)
                .collect(Collectors.toList());
    }
}
