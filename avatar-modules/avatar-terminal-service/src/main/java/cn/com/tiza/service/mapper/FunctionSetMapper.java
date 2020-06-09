package cn.com.tiza.service.mapper;


import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.domain.FunctionSet;
import cn.com.tiza.service.dto.FunctionSetDto;
import cn.com.tiza.web.vm.FunctionSetVM;
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
public class FunctionSetMapper {

    public FunctionSet dtoToEntity(FunctionSetDto dto) {
        if (dto == null) {
            return null;
        }
        FunctionSet entity = new FunctionSet();
        entity.setId(dto.getId());
        entity.setCode(dto.getCode());
        entity.setCreateTime(System.currentTimeMillis());
        entity.setCreateUserAccount(BaseContextHandler.getName());
        entity.setFunctionType(dto.getFunctionType());
        entity.setName(dto.getName());
        entity.setRemark(dto.getRemark());
        return entity;
    }

    public FunctionSetVM toVM(FunctionSet entity) {
        if (entity == null) {
            return null;
        }
        FunctionSetVM vm = new FunctionSetVM();
        vm.setId(entity.getId());
        vm.setCode(entity.getCode());
        vm.setCreateTime(entity.getCreateTime());
        vm.setCreateUserAccount(entity.getCreateUserAccount());
        vm.setFunctionType(entity.getFunctionType());
        vm.setName(entity.getName());
        vm.setRemark(entity.getRemark());
        return vm;
    }

    public List<FunctionSetVM> entitiesToVMList(List<FunctionSet> entities) {
        return entities.stream()
                .map(this::toVM)
                .collect(Collectors.toList());
    }
}
