package cn.com.tiza.service.mapper;


import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.domain.Charge;
import cn.com.tiza.dto.UserType;
import cn.com.tiza.service.dto.ChargeDto;
import cn.com.tiza.web.rest.vm.ChargeVM;
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
public class ChargeMapper {

    public Charge dtoToEntity(ChargeDto dto) {
        if (dto == null) {
            return null;
        }
        Charge entity = new Charge();
        entity.setConfigFlag(0);
        String userType = BaseContextHandler.getUserType();
        if (userType.equals(UserType.ADMIN.name())) {
            entity.setOrganizationId(dto.getOrganizationId());
        } else {
            entity.setOrganizationId(BaseContextHandler.getRootOrgId());
        }
        entity.setTerminalModel(dto.getTerminalModel());
        return entity;
    }

    public ChargeVM toVM(Charge entity) {
        if (entity == null) {
            return null;
        }
        ChargeVM vm = new ChargeVM();
        vm.setId(entity.getId());
        vm.setConfigFlag(entity.getConfigFlag());
        vm.setTerminalModel(entity.getTerminalModel());
        return vm;
    }

    public List<ChargeVM> entitiesToVMList(List<Charge> entities) {
        return entities.stream()
                .map(this::toVM)
                .collect(Collectors.toList());
    }
}
