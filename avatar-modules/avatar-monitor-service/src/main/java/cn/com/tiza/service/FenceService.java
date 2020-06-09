package cn.com.tiza.service;


import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dao.AlarmHistoryDao;
import cn.com.tiza.dao.AlarmInfoDao;
import cn.com.tiza.dao.FenceDao;
import cn.com.tiza.dao.FenceVehicleDao;
import cn.com.tiza.domain.AlarmHistory;
import cn.com.tiza.domain.AlarmInfo;
import cn.com.tiza.domain.Fence;
import cn.com.tiza.domain.FenceVehicle;
import cn.com.tiza.dto.AlarmType;
import cn.com.tiza.dto.UserType;
import cn.com.tiza.service.dto.FenceDto;
import cn.com.tiza.service.dto.FenceQuery;
import cn.com.tiza.service.dto.FenceVehicleQuery;
import cn.com.tiza.service.mapper.FenceMapper;
import cn.com.tiza.util.EntityValidator;
import cn.com.tiza.web.rest.errors.BadRequestException;
import cn.com.tiza.web.rest.errors.ErrorConstants;
import cn.com.tiza.web.rest.vm.FenceVM;
import cn.com.tiza.web.rest.vm.VehicleVM;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class FenceService {

    @Autowired
    private FenceDao fenceDao;

    @Autowired
    private FenceVehicleDao fenceVehicleDao;

    @Autowired
    private FenceMapper fenceMapper;

    @Autowired
    private AlarmHistoryDao alarmHistoryDao;

    @Autowired
    private AlarmInfoDao alarmInfoDao;

    public PageQuery<FenceVM> findAll(FenceQuery query) {
        PageQuery<FenceVM> pageQuery = query.toPageQuery();
        fenceDao.pageQuery(pageQuery);
        return pageQuery;
    }

    public Optional<Fence> get(Long id) {
        return Optional.ofNullable(fenceDao.single(id));
    }

    public Fence create(FenceDto command) {
        //管理员不可创建
        if (BaseContextHandler.getUserType().equals(UserType.ADMIN.name())) {
            throw new BadRequestException(ErrorConstants.ADMIN_CAN_NOT_OPERATE_FENCE);
        }
        checkNameExist(null, BaseContextHandler.getOrgId(), command.getName());
        Fence entity = fenceMapper.dtoToEntity(command);
        fenceDao.insert(entity);
        return entity;
    }

    private void checkNameExist(Long id, Long orgId, String name) {
        Fence single = fenceDao.createLambdaQuery()
                .andEq(Fence::getOrganizationId, orgId)
                .andEq(Fence::getName, name)
                .single();
        EntityValidator.checkUnique(Optional.ofNullable(single), id, ErrorConstants.FENCE_NAME_EXIST);
    }

    public Optional<Fence> update(Long id, FenceDto command) {
        if (BaseContextHandler.getUserType().equals(UserType.ADMIN.name())) {
            throw new BadRequestException(ErrorConstants.ADMIN_CAN_NOT_OPERATE_FENCE);
        }
        return get(id).map(entity -> {
            checkNameExist(id, BaseContextHandler.getOrgId(), command.getName());
            entity.setAlarmType(command.getAlarmType());
            entity.setFenceType(command.getFenceType());
            entity.setArea(command.getArea());
            entity.setName(command.getName());
            entity.setUpdateTime(System.currentTimeMillis());
            entity.setUpdateUserAccount(BaseContextHandler.getLoginName());
            entity.setUpdateUserRealname(BaseContextHandler.getName());
            fenceDao.updateById(entity);
            return entity;
        });
    }

    public void delete(Long id) {
        fenceDao.deleteById(id);
        fenceVehicleDao.createLambdaQuery()
                .andEq(FenceVehicle::getFenceId, id)
                .delete();
        alarmInfoDao.createLambdaQuery()
                .andEq(AlarmInfo::getAlarmType, AlarmType.FENCE)
                .andEq(AlarmInfo::getFenceId,id)
                .delete();
        alarmHistoryDao.createLambdaQuery()
                .andEq(AlarmHistory::getAlarmType, AlarmType.FENCE)
                .andEq(AlarmHistory::getFenceId,id)
                .delete();
    }

    public void delete(Long[] ids) {
        if (BaseContextHandler.getUserType().equals(UserType.ADMIN.name())) {
            throw new BadRequestException(ErrorConstants.ADMIN_CAN_NOT_OPERATE_FENCE);
        }
        for (Long id : ids) {
            delete(id);
        }
    }

    public PageQuery<VehicleVM> pageQueryUnRelatedVehicles(FenceVehicleQuery query) {
        PageQuery<VehicleVM> pageQuery = query.toPageQuery();
        fenceDao.pageQueryUnRelatedVehicles(pageQuery);
        return pageQuery;
    }

    public PageQuery<VehicleVM> pageQueryRelatedVehicles(FenceVehicleQuery query) {
        PageQuery<VehicleVM> pageQuery = query.toPageQuery();
        fenceDao.pageQueryRelatedVehicles(pageQuery);
        return pageQuery;
    }

    public void deleteRelatedVehiclesByOrgId(String vin, Long orgId) {
        List<Long> fenceIds = fenceDao.createLambdaQuery()
                .andEq(Fence::getOrganizationId, orgId)
                .select("id")
                .stream().map(Fence::getId)
                .collect(Collectors.toList());
        if (fenceIds.isEmpty()) {
            return;
        }
        fenceVehicleDao.createLambdaQuery()
                .andEq(FenceVehicle::getVin, vin)
                .andIn(FenceVehicle::getFenceId, fenceIds)
                .delete();
    }

    public void relateVehicles(Long fenceId, List<String> vinList) {
        if (BaseContextHandler.getUserType().equals(UserType.ADMIN.name())) {
            throw new BadRequestException(ErrorConstants.ADMIN_CAN_NOT_OPERATE_FENCE);
        }
        get(fenceId).map(fence -> {
            fence.setVehicleNum(vinList.size());
            fenceDao.updateById(fence);
            return fence;
        }).orElseThrow(() -> new BadRequestException(ErrorConstants.ENTITY_NOT_FOUND_TYPE));

       /* fenceVehicleDao.createLambdaQuery()
                .andEq(FenceVehicle::getFenceId, fenceId)
                .delete();*/
        if ( vinList.isEmpty()) {
            return;
        }
        fenceVehicleDao.insertBatch(vinList.stream().map(vin -> {
            FenceVehicle fenceVehicle = new FenceVehicle();
            fenceVehicle.setVin(vin);
            fenceVehicle.setFenceId(fenceId);
            return fenceVehicle;
        }).collect(Collectors.toList()));
    }

    public void unRelateVehicles(Long fenceId, List<String> vinList) {
        if (BaseContextHandler.getUserType().equals(UserType.ADMIN.name())) {
            throw new BadRequestException(ErrorConstants.ADMIN_CAN_NOT_OPERATE_FENCE);
        }
        if (Objects.isNull(vinList) || vinList.isEmpty()) {
            return;
        }
        get(fenceId).map(fence -> {
            fence.setVehicleNum(fence.getVehicleNum() - vinList.size());
            fenceDao.updateById(fence);
            return fence;
        }).orElseThrow(() -> new BadRequestException(ErrorConstants.ENTITY_NOT_FOUND_TYPE));
        fenceVehicleDao.createLambdaQuery()
                .andEq(FenceVehicle::getFenceId, fenceId)
                .andIn(FenceVehicle::getVin, vinList)
                .delete();
    }
}
