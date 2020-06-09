package cn.com.tiza.service.mapper;


import cn.com.tiza.context.BaseContextHandler;
import org.springframework.stereotype.Component;

import cn.com.tiza.domain.FaultDict;
import cn.com.tiza.service.dto.FaultDictDto;
import cn.com.tiza.web.rest.vm.FaultDictVM;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Component
public class FaultDictMapper {

    public FaultDict dtoToEntity(FaultDictDto dto) {
        if (dto == null) {
            return null;
        }
        FaultDict entity = new FaultDict();
        entity.setFileName(dto.getFileName());
        entity.setName(dto.getName());
        entity.setOrganizationId(dto.getOrganizationId());
        entity.setCreateTime(System.currentTimeMillis());
        entity.setCreateUserAccount(BaseContextHandler.getLoginName());
        entity.setCreateUserRealname(BaseContextHandler.getName());
        entity.setUpdateTime(System.currentTimeMillis());
        entity.setUpdateUserAccount(BaseContextHandler.getLoginName());
        entity.setUpdateUserRealname(BaseContextHandler.getName());
        return entity;
    }

    public FaultDictVM toVM(FaultDict entity) {
        if (entity == null) {
            return null;
        }
        FaultDictVM vm = new FaultDictVM();
        vm.setId(entity.getId());
        vm.setFileName(entity.getFileName());
        vm.setName(entity.getName());
        vm.setOrganizationId(entity.getOrganizationId());
        vm.setUpdateTime(entity.getUpdateTime());
        vm.setUpdateUserAccount(entity.getUpdateUserAccount());
        vm.setUpdateUserRealname(entity.getUpdateUserRealname());
        return vm;
    }

    public List<FaultDictVM> entitiesToVMList(List<FaultDict> entities) {
        return entities.stream()
                .map(this::toVM)
                .collect(Collectors.toList());
    }
}
