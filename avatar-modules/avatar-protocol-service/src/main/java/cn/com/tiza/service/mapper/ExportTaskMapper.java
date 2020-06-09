package cn.com.tiza.service.mapper;

import org.springframework.stereotype.Component;

import cn.com.tiza.domain.ExportTask;
import cn.com.tiza.service.dto.ExportTaskDto;
import cn.com.tiza.web.rest.vm.ExportTaskVM;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 
 * @author villas
 */
@Component
public class ExportTaskMapper {

    public ExportTask dtoToEntity(ExportTaskDto dto) {
        if (dto == null) {
            return null;
        }
        ExportTask entity = new ExportTask();
        entity.setBeginTime(dto.getBeginTime());
        entity.setEndTime(Optional.ofNullable(dto.getEndTime()).orElse(System.currentTimeMillis()));
        entity.setFilePath(dto.getFilePath());
        entity.setFwpTaskId(dto.getFwpTaskId());
        entity.setName(dto.getName());
        entity.setStatus(ExportTask.ORIGINAL);
        entity.setVehicleAmount(dto.getInputVinList().size());
        entity.setDataType(dto.getDataType());
        return entity;
    }

    public ExportTask copyProps(ExportTaskDto dto, ExportTask entity) {
        entity.setBeginTime(dto.getBeginTime());
        entity.setEndTime(dto.getEndTime());
        entity.setFilePath(dto.getFilePath());
        entity.setFwpTaskId(dto.getFwpTaskId());
        entity.setName(dto.getName());
        entity.setStatus(dto.getStatus());
        entity.setDataType(dto.getDataType());
        entity.setVehicleAmount(dto.getVehicleAmount());
        return entity;
    }

    public ExportTaskVM toVM(ExportTask entity) {
        if (entity == null) {
            return null;
        }
        ExportTaskVM vm = new ExportTaskVM();
        vm.setId(entity.getId());
        vm.setBeginTime(entity.getBeginTime());
        vm.setEndTime(entity.getEndTime());
        vm.setFilePath(entity.getFilePath());
        vm.setFwpTaskId(entity.getFwpTaskId());
        vm.setName(entity.getName());
        vm.setStatus(entity.getStatus());
        vm.setDataType(entity.getDataType());
        vm.setVehicleAmount(entity.getVehicleAmount());
        return vm;
    }

    public List<ExportTaskVM> entitiesToVMList(List<ExportTask> entities) {
        return entities.stream().map(this::toVM).collect(Collectors.toList());
    }
}
