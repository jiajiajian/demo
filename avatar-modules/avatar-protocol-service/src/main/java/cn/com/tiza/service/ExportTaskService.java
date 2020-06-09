package cn.com.tiza.service;

import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dao.ExportTaskVehicleDao;
import cn.com.tiza.domain.ExportTaskVehicle;
import cn.com.tiza.web.rest.errors.BadRequestAlertException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.tiza.domain.ExportTask;
import cn.com.tiza.service.dto.ExportTaskDto;
import cn.com.tiza.service.mapper.ExportTaskMapper;
import cn.com.tiza.dao.ExportTaskDao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 
 * @author villas
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ExportTaskService {

    @Autowired
    private ExportTaskDao exportTaskDao;

    @Autowired
    private ExportTaskVehicleDao exportTaskVehicleDao;

    @Autowired
    private ExportTaskMapper exportTaskMapper;

    public List<ExportTask> findAll() {
        return exportTaskDao.findAll(BaseContextHandler.getOrgId());
    }

    public Optional<ExportTask> get(Long id) {
        return Optional.ofNullable(exportTaskDao.single(id));
    }

    public ExportTask create(ExportTaskDto command) {
        ExportTask entity = exportTaskMapper.dtoToEntity(command);
        entity.setBeginTime(Optional.ofNullable(command.getBeginTime()).orElseGet(() -> this.getBindTime(command)));
        exportTaskDao.insert(entity);
        exportTaskVehicleDao.insertBatch(command.getInputVinList().stream().map(vin -> {
            ExportTaskVehicle etv = new ExportTaskVehicle();
            etv.setVin(vin);
            etv.setExportTaskId(entity.getId());
            return etv;
        }).collect(Collectors.toList()));
        return entity;
    }

    public Optional<ExportTask> update(Long id, ExportTaskDto command) {
        return get(id)
            .map(entity -> exportTaskMapper.copyProps(command, entity)).map(entity -> {
                entity.setStatus(ExportTask.ORIGINAL);
                exportTaskDao.updateById(entity);
                return entity;
            });
    }

    public void delete(Long id) {
        if (exportTaskDao.single(id).getStatus() == ExportTask.RUNNING) {
            throw new BadRequestAlertException("the task is running", String.valueOf(id), "no.delete.task.running");
        }
        exportTaskVehicleDao.deleteByExportTaskId(id);
        exportTaskDao.deleteById(id);
    }

    private Long getBindTime(ExportTaskDto command) {
        String vin = command.getInputVinList().get(0);
        Long taskId = command.getFwpTaskId();
        return exportTaskVehicleDao.queryCreateTime(vin, taskId);
    }
}
