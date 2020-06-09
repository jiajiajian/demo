package cn.com.tiza.service;


import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dto.UserType;
import cn.com.tiza.web.rest.errors.BadRequestException;
import cn.com.tiza.web.rest.errors.ErrorConstants;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.tiza.domain.VehicleMaintenance;
import cn.com.tiza.service.dto.VehicleMaintenanceDto;
import cn.com.tiza.service.dto.VehicleMaintenanceQuery;
import cn.com.tiza.service.mapper.VehicleMaintenanceMapper;
import cn.com.tiza.web.rest.vm.VehicleMaintenanceVM;
import cn.com.tiza.dao.VehicleMaintenanceDao;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service
 * gen by beetlsql 2020-03-31
 *
 * @author tiza
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class VehicleMaintenanceService {

    @Autowired
    private VehicleMaintenanceDao vehicleMaintenanceDao;

    @Autowired
    private VehicleMaintenanceMapper vehicleMaintenanceMapper;

    public PageQuery<VehicleMaintenanceVM> findAll(VehicleMaintenanceQuery query) {
        if (Objects.isNull(query.getOrganizationId())){
            query.setOrganizationId(BaseContextHandler.getOrgId());
        }
        PageQuery pageQuery = query.toPageQuery();
        vehicleMaintenanceDao.pageQuery(pageQuery);
        return pageQuery;
    }

    public Optional<VehicleMaintenance> get(Long id) {
        return Optional.ofNullable(vehicleMaintenanceDao.single(id));
    }

    public VehicleMaintenance create(VehicleMaintenanceDto command) {
        String userType = BaseContextHandler.getUserType();
        if ("ADMIN".equals(userType)) {
            throw new BadRequestException(ErrorConstants.AUTH_NOT_ALLOW);
        }
        List<String> content = command.getContent();
        String itemDetail = content.stream().collect(Collectors.joining(","));
        command.setItemDetail(itemDetail);
        command.setCreateTime(System.currentTimeMillis());
        command.setCreateUserAccount(BaseContextHandler.getLoginName());
        command.setCreateUserRealname(BaseContextHandler.getName());
        command.setOrganizationId(BaseContextHandler.getOrgId());
        VehicleMaintenance entity = vehicleMaintenanceMapper.dtoToEntity(command);
        vehicleMaintenanceDao.insert(entity);
        return entity;
    }

    public Optional<VehicleMaintenance> update(Long id, VehicleMaintenanceDto command) {
        List<String> content = command.getContent();
        String itemDetail = content.stream().collect(Collectors.joining(","));
        return get(id).map(entity -> {
            entity.setId(command.getId());
            entity.setDescription(command.getDescription());
            entity.setItemDetail(itemDetail);
            entity.setItemName(command.getItemName());
            entity.setOrganizationId(BaseContextHandler.getOrgId());
            entity.setUpdateTime(System.currentTimeMillis());
            entity.setUpdateUserAccount(BaseContextHandler.getLoginName());
            entity.setUpdateUserRealname(BaseContextHandler.getName());
            vehicleMaintenanceDao.updateById(entity);
            return entity;
        });
    }

    public void delete(Long id) {
        vehicleMaintenanceDao.deleteById(id);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            delete(id);
        }
    }
}
