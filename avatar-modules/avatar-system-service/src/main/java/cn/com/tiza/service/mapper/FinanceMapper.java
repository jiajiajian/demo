package cn.com.tiza.service.mapper;


import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import cn.com.tiza.domain.Finance;
import cn.com.tiza.service.dto.FinanceDto;
import cn.com.tiza.web.rest.vm.FinanceVM;

import java.util.List;
import java.util.stream.Collectors;

/**
*  Mapper
* gen by beetlsql 2020-03-06
* @author tiza
*/
@Component
public class FinanceMapper {

    public Finance dtoToEntity(FinanceDto dto) {
        if(dto == null) {
            return null;
        }
        Finance entity = new Finance();
        BeanUtils.copyProperties(dto, entity);
        entity.setCreatorInfo();
        entity.setUpdateInfo();
        return entity;
    }

    public FinanceVM toVM(Finance entity) {
        if(entity == null) {
            return null;
        }
        FinanceVM vm = new FinanceVM();
        BeanUtils.copyProperties(entity, vm);
        return vm;
    }

    public List<FinanceVM> entitiesToVMList(List<Finance> entities) {
        return entities.stream()
                .map(this::toVM)
                .collect(Collectors.toList());
    }
}
