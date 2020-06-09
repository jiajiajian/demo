package cn.com.tiza.service.mapper;


import cn.com.tiza.domain.SoftVersion;
import cn.com.tiza.service.dto.SoftVersionDto;
import cn.com.tiza.web.vm.SoftVersionVM;
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
public class SoftVersionMapper {

    public SoftVersion dtoToEntity(SoftVersionDto dto) {
        if (dto == null) {
            return null;
        }
        SoftVersion entity = new SoftVersion();
        entity.setCode(dto.getCode());
        entity.setCollectFunctionId(dto.getCollectFunctionId());
        entity.setCreateTime(System.currentTimeMillis());
        entity.setLockFunctionId(dto.getLockFunctionId());
        entity.setName(dto.getName());
        entity.setRemark(dto.getRemark());
        return entity;
    }

    public SoftVersionVM toVM(SoftVersion entity) {
        if (entity == null) {
            return null;
        }
        SoftVersionVM vm = new SoftVersionVM();
        vm.setId(entity.getId());
        vm.setCode(entity.getCode());
        vm.setCollectFunctionId(entity.getCollectFunctionId());
        vm.setCreateTime(entity.getCreateTime());
        vm.setLockFunctionId(entity.getLockFunctionId());
        vm.setName(entity.getName());
        vm.setRemark(entity.getRemark());
        vm.setCollectName(entity.getCollectName());
        vm.setLockName(entity.getLockName());
        return vm;
    }

    public List<SoftVersionVM> entitiesToVMList(List<SoftVersion> entities) {
        return entities.stream()
                .map(this::toVM)
                .collect(Collectors.toList());
    }
}
