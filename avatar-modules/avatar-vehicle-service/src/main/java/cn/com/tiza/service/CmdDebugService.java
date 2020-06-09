package cn.com.tiza.service;


import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dao.CmdDebugDao;
import cn.com.tiza.dao.VehicleTypeDao;
import cn.com.tiza.domain.CmdDebug;
import cn.com.tiza.domain.VehicleType;
import cn.com.tiza.service.dto.CmdDebugDto;
import cn.com.tiza.service.dto.CmdDebugQuery;
import cn.com.tiza.service.mapper.CmdDebugMapper;
import cn.com.tiza.web.rest.errors.BadRequestException;
import cn.com.tiza.web.rest.errors.ErrorConstants;
import cn.com.tiza.web.rest.vm.CmdDebugVM;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

/**
 * Service
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class CmdDebugService {

    @Autowired
    private CmdDebugDao cmdDebugDao;

    @Autowired
    private CmdDebugMapper cmdDebugMapper;

    @Autowired
    private VehicleTypeDao vehicleTypeDao;

    public PageQuery<CmdDebugVM> findAll(CmdDebugQuery query) {
        PageQuery<CmdDebugVM> pageQuery = query.toPageQuery();
        cmdDebugDao.pageQuery(pageQuery);
        pageQuery.getList()
                .forEach(vm -> {
                    if (org.springframework.util.StringUtils.hasText(vm.getCmd())) {
                        vm.setCmdNum(vm.getCmd().split(",").length);
                    }
                });
        return pageQuery;
    }

    public Optional<CmdDebug> get(Long id) {
        return Optional.ofNullable(cmdDebugDao.single(id));
    }

    public CmdDebug create(CmdDebugDto command) {
        //已经创建配置的一级机构不可以再次创建
        CmdDebug byRootOrg = cmdDebugDao.findByRootOrg(command.getOrganizationId(),command.getVehicleTypeId());
        if (Objects.nonNull(byRootOrg)) {
            throw new BadRequestException(ErrorConstants.CMD_DEBUG_HAS_CREATED_FOR_THIS_ORGANIZATION_VEHICLE_TYPE);
        }
        //校验车型是否存在
        VehicleType vehicleType = vehicleTypeDao.single(command.getVehicleTypeId());
        if (Objects.isNull(vehicleType)) {
            throw new BadRequestException(ErrorConstants.VEHICLE_MODEL_NOT_EXIST);
        }
        CmdDebug entity = cmdDebugMapper.dtoToEntity(command);
        cmdDebugDao.insert(entity);
        return entity;
    }

    public Optional<CmdDebug> update(Long id, CmdDebugDto command) {
        return get(id).map(entity -> {
            entity.setCmd(StringUtils.join(command.getCmdList(), ","));
            entity.setRemark(command.getRemark());
            entity.setVehicleTypeId(command.getVehicleTypeId());
            entity.setUpdateTime(System.currentTimeMillis());
            entity.setUpdateUserAccount(BaseContextHandler.getLoginName());
            entity.setUpdateUserRealname(BaseContextHandler.getName());
            cmdDebugDao.updateById(entity);
            return entity;
        });
    }

    public void delete(Long id) {
        cmdDebugDao.deleteById(id);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            delete(id);
        }
    }
}
