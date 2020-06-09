package cn.com.tiza.service.mapper;


import cn.com.tiza.domain.FunctionSetItem;
import cn.com.tiza.service.dto.FunctionSetItemDto;
import cn.com.tiza.web.vm.FunctionSetItemVM;
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
public class FunctionSetItemMapper {

    public FunctionSetItem dtoToEntity(FunctionSetItemDto dto) {
        if(dto == null) {
            return null;
        }
        FunctionSetItem entity = new FunctionSetItem();
        entity.setBitLength(dto.getBitLength());
        entity.setBitStart(dto.getBitStart());
        entity.setCode(dto.getCode());
        entity.setDataFormat(dto.getDataFormat());
        entity.setEnName(dto.getEnName());
        entity.setFunctionId(dto.getFunctionId());
        entity.setName(dto.getName());
        entity.setOffset(dto.getOffset());
        entity.setRate(dto.getRate());
        entity.setUnit(dto.getUnit());
        entity.setDescription(dto.getDescription());
        entity.setVarAddress(dto.getVarAddress());
        entity.setSeparator(dto.getSeparator());
        entity.setSortNum(dto.getSortNum());
        return entity;
    }

    public FunctionSetItemVM toVM(FunctionSetItem entity) {
        if(entity == null) {
            return null;
        }
        FunctionSetItemVM vm = new FunctionSetItemVM();
        vm.setId(entity.getId());
        vm.setBitLength(entity.getBitLength());
        vm.setBitStart(entity.getBitStart());
        vm.setCode(entity.getCode());
        vm.setDataFormat(entity.getDataFormat());
        vm.setEnName(entity.getEnName());
        vm.setFunctionId(entity.getFunctionId());
        vm.setName(entity.getName());
        vm.setOffset(entity.getOffset());
        vm.setRate(entity.getRate());
        vm.setUnit(entity.getUnit());
        vm.setDescription(entity.getDescription());
        vm.setVarAddress(entity.getVarAddress());
        vm.setSeparator(entity.getSeparator());
        vm.setSortNum(entity.getSortNum());
        return vm;
    }

    public List<FunctionSetItemVM> entitiesToVMList(List<FunctionSetItem> entities) {
        return entities.stream()
                .map(this::toVM)
                .collect(Collectors.toList());
    }
}
