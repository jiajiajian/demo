package cn.com.tiza.service.mapper;


import cn.com.tiza.domain.FunctionSetItemLock;
import cn.com.tiza.service.dto.FunctionSetItemLockDto;
import cn.com.tiza.web.vm.FunctionSetItemLockVM;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper
 * gen by beetlsql 2020-03-31
 *
 * @author tiza
 */
@Component
public class FunctionSetItemLockMapper {

    public FunctionSetItemLock dtoToEntity(FunctionSetItemLockDto dto) {
        if (dto == null) {
            return null;
        }
        FunctionSetItemLock entity = new FunctionSetItemLock();
        entity.setChinaName(dto.getChinaName());
        entity.setCode(dto.getCode());
        entity.setDicItemId(dto.getDicItemId());
        entity.setEnglishName(dto.getEnglishName());
        entity.setFunctionId(dto.getFunctionId());
        entity.setMessage(dto.getMessage());
        return entity;
    }

    public FunctionSetItemLockVM toVM(FunctionSetItemLock entity) {
        if (entity == null) {
            return null;
        }
        FunctionSetItemLockVM vm = new FunctionSetItemLockVM();
        vm.setId(entity.getId());
        vm.setChinaName(entity.getChinaName());
        vm.setCode(entity.getCode());
        vm.setDicItemId(entity.getDicItemId());
        vm.setEnglishName(entity.getEnglishName());
        vm.setFunctionId(entity.getFunctionId());
        vm.setTypeName(entity.getTypeName());
        vm.setMessage(entity.getMessage());
        return vm;
    }

    public List<FunctionSetItemLockVM> entitiesToVMList(List<FunctionSetItemLock> entities) {
        return entities.stream()
                .map(this::toVM)
                .collect(Collectors.toList());
    }
}
