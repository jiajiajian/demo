package cn.com.tiza.service;


import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.tiza.domain.MaintenanceBind;
import cn.com.tiza.service.dto.MaintenanceBindDto;
import cn.com.tiza.service.dto.MaintenanceBindQuery;
import cn.com.tiza.service.mapper.MaintenanceBindMapper;
import cn.com.tiza.web.rest.vm.MaintenanceBindVM;
import cn.com.tiza.dao.MaintenanceBindDao;

import java.util.List;
import java.util.Optional;

/**
*  Service
* gen by beetlsql 2020-04-01
* @author tiza
*/
@Slf4j
@Service
@Transactional
public class MaintenanceBindService {

    @Autowired
    private MaintenanceBindDao maintenanceBindDao;

    @Autowired
    private MaintenanceBindMapper maintenanceBindMapper;


    public Optional<MaintenanceBind> get(Long id) {
        return Optional.ofNullable(maintenanceBindDao.single(id));
    }

    public MaintenanceBind create(MaintenanceBindDto command) {
        MaintenanceBind entity = maintenanceBindMapper.dtoToEntity(command);
        maintenanceBindDao.insert(entity);
        return entity;
    }

    public Optional<MaintenanceBind> update(Long id, MaintenanceBindDto command) {
        return get(id).map(entity -> {
                            entity.setId(command.getId());
                            entity.setCreateTime(command.getCreateTime());
                            entity.setCreateUserAccount(command.getCreateUserAccount());
                            entity.setCreateUserRealname(command.getCreateUserRealname());
                            entity.setMaintenanceTacticsId(command.getMaintenanceTacticsId());
                            entity.setVehicleModelId(command.getVehicleModelId());
                            entity.setVehicleTypeId(command.getVehicleTypeId());
                            return entity;
                        });
    }

    public void delete(Long id) {
        maintenanceBindDao.deleteById(id);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            delete(id);
        }
    }
}
