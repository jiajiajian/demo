package cn.com.tiza.service.mapper;


import cn.com.tiza.context.BaseContextHandler;
import org.springframework.stereotype.Component;

import cn.com.tiza.domain.Tla;
import cn.com.tiza.service.dto.TlaDto;
import cn.com.tiza.web.rest.vm.TlaVM;

import java.util.List;
import java.util.stream.Collectors;

/**
*  Mapper
* gen by beetlsql 2020-05-12
* @author tiza
*/
@Component
public class TlaMapper {

    public Tla dtoToEntity(TlaDto dto) {
        if(dto == null) {
            return null;
        }
        Tla entity = new Tla();
        entity.setCreateTime(System.currentTimeMillis());
        entity.setCreateUserAccount(BaseContextHandler.getLoginName());
        entity.setOrganizationId(dto.getOrganizationId());
        entity.setTla(dto.getTla());
        entity.setTlaId(dto.getTlaId());
        return entity;
    }

    public TlaVM toVM(Tla entity) {
        if(entity == null) {
            return null;
        }
        TlaVM vm = new TlaVM();
        vm.setId(entity.getId());
        vm.setCreateTime(entity.getCreateTime());
        vm.setCreateUserAccount(entity.getCreateUserAccount());
        vm.setOrganizationId(entity.getOrganizationId());
        vm.setTla(entity.getTla());
        vm.setTlaId(entity.getTlaId());
        return vm;
    }

    public List<TlaVM> entitiesToVMList(List<Tla> entities) {
        return entities.stream()
                .map(this::toVM)
                .collect(Collectors.toList());
    }
}
