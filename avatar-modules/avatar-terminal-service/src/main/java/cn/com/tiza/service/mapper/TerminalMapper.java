package cn.com.tiza.service.mapper;


import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.domain.Terminal;
import cn.com.tiza.service.dto.TerminalDto;
import cn.com.tiza.web.rest.dto.VehicleDto;
import cn.com.tiza.web.vm.TerminalVM;
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
public class TerminalMapper {

    public Terminal dtoToEntity(TerminalDto dto) {
        if (dto == null) {
            return null;
        }
        Terminal entity = new Terminal();
        entity.setProduceDate(dto.getProduceDate());
        entity.setCode(dto.getCode());
        entity.setCreateTime(System.currentTimeMillis());
        entity.setCreateUserAccount(BaseContextHandler.getLoginName());
        entity.setFirmWireVersion(dto.getFirmWireVersion());
        entity.setProtocolId(dto.getProtocolId());
        entity.setSimcardId(dto.getSimcardId());
        entity.setSoftVersionId(dto.getSoftVersionId());
        entity.setTerminalModel(dto.getTerminalModel());
        return entity;
    }

    public TerminalVM toVM(Terminal entity) {
        if (entity == null) {
            return null;
        }
        TerminalVM vm = new TerminalVM();
        vm.setId(entity.getId());
        vm.setProduceDate(entity.getProduceDate());
        vm.setCode(entity.getCode());
        vm.setCreateTime(entity.getCreateTime());
        vm.setCreateUserAccount(entity.getCreateUserAccount());
        vm.setFirmWireVersion(entity.getFirmWireVersion());
        vm.setProtocolId(entity.getProtocolId());
        vm.setSimcardId(entity.getSimcardId());
        vm.setSoftVersionId(entity.getSoftVersionId());
        vm.setTerminalModel(entity.getTerminalModel());
        vm.setSimCode(entity.getSimCode());
        vm.setSoftName(entity.getSoftName());
        vm.setCollectFunctionId(entity.getCollectFunctionId());
        vm.setLockFunctionId(entity.getLockFunctionId());
        vm.setProtocolName(entity.getProtocolName());
        vm.setCreateTimeFormat(entity.getCreateTimeFormat());
        vm.setProduceDateFormat(entity.getProduceDateFormat());
        vm.setCollectName(entity.getCollectName());
        vm.setLockName(entity.getLockName());
        return vm;
    }

    public List<TerminalVM> entitiesToVMList(List<Terminal> entities) {
        return entities.stream()
                .map(this::toVM)
                .collect(Collectors.toList());
    }

    public VehicleDto toVehicleDto(TerminalDto t) {
        VehicleDto dto = new VehicleDto();
        dto.setVin(t.getCode());
        dto.setDictId(t.getProtocolId());
        dto.setOrganizationId(t.getOrganizationId());
        dto.setTerminalId(t.getId());
        dto.setSimCard(t.getSimCode());
        return dto;
    }
}
