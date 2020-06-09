package cn.com.tiza.service;


import cn.com.tiza.context.BaseContextHandler;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.tiza.domain.VehicleDebugLog;
import cn.com.tiza.service.dto.VehicleDebugLogDto;
import cn.com.tiza.service.dto.VehicleDebugLogQuery;
import cn.com.tiza.service.mapper.VehicleDebugLogMapper;
import cn.com.tiza.web.rest.vm.VehicleDebugLogVM;
import cn.com.tiza.dao.VehicleDebugLogDao;

import java.util.List;
import java.util.Optional;

/**
*  Service
* gen by beetlsql 2020-03-17
* @author tiza
*/
@Slf4j
@Service
@Transactional
public class VehicleDebugLogService {

    @Autowired
    private VehicleDebugLogDao vehicleDebugLogDao;

    @Autowired
    private VehicleDebugLogMapper vehicleDebugLogMapper;

    public PageQuery<VehicleDebugLog> findAll(VehicleDebugLogQuery query) {
        PageQuery pageQuery = query.toPageQuery();
        vehicleDebugLogDao.pageQuery(pageQuery);
        return pageQuery;
    }

    public Optional<VehicleDebugLog> get(Long id) {
        return Optional.ofNullable(vehicleDebugLogDao.single(id));
    }

    public VehicleDebugLog create(VehicleDebugLogDto command) {
        VehicleDebugLog entity = vehicleDebugLogMapper.dtoToEntity(command);
        entity.setCreateTime(System.currentTimeMillis());
        entity.setCreateUserAccount(BaseContextHandler.getLoginName());
        entity.setCreateUserRealname(BaseContextHandler.getName());
        entity.setDebugTime(System.currentTimeMillis());
        entity.setDebugUserId(BaseContextHandler.getUserID());
        vehicleDebugLogDao.insert(entity);
        return entity;
    }

    public Optional<VehicleDebugLog> update(Long id, VehicleDebugLogDto command) {
        return get(id).map(entity -> {
                            entity.setStatus(command.getStatus());
                            entity.setContent(command.getContent());
                            entity.setDebugTime(System.currentTimeMillis());
                            entity.setDebugUserId(BaseContextHandler.getUserID());
                            entity.setItemKey(command.getItemKey());
                            entity.setItemName(command.getItemName());
                            entity.setVin(command.getVin());
                            return entity;
                        });
    }

    public void delete(Long id) {
        vehicleDebugLogDao.deleteById(id);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            delete(id);
        }
    }
}
