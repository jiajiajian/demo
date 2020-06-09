package cn.com.tiza.service.mapper;


import org.springframework.stereotype.Component;

import cn.com.tiza.domain.Lock;
import cn.com.tiza.service.dto.LockDto;
import cn.com.tiza.web.vm.LockVM;

import java.util.List;
import java.util.stream.Collectors;

/**
*  Mapper
* gen by beetlsql 2020-03-31
* @author tiza
*/
@Component
public class LockMapper {

    public Lock dtoToEntity(LockDto dto) {
        if(dto == null) {
            return null;
        }
        Lock entity = new Lock();
        entity.setId(dto.getId());
        entity.setVin(dto.getVin());
        return entity;
    }

    public LockVM toVM(Lock entity) {
        if(entity == null) {
            return null;
        }
        LockVM vm = new LockVM();
        vm.setId(entity.getId());
        vm.setVin(entity.getVin());
        return vm;
    }

    public List<LockVM> entitiesToVMList(List<Lock> entities) {
        return entities.stream()
                .map(this::toVM)
                .collect(Collectors.toList());
    }
}
