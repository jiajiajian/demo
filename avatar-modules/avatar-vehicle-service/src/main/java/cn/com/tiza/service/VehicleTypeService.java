package cn.com.tiza.service;


import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dao.CmdDebugDao;
import cn.com.tiza.dao.VehicleModelDao;
import cn.com.tiza.dao.VehicleTypeDao;
import cn.com.tiza.domain.VehicleType;
import cn.com.tiza.dto.UserType;
import cn.com.tiza.service.dto.VehicleTypeDto;
import cn.com.tiza.service.dto.VehicleTypeQuery;
import cn.com.tiza.service.mapper.VehicleTypeMapper;
import cn.com.tiza.web.rest.errors.BadRequestException;
import cn.com.tiza.web.rest.errors.ErrorConstants;
import cn.com.tiza.web.rest.vm.OptionVM;
import cn.com.tiza.web.rest.vm.VehicleTypeVM;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.com.tiza.util.EntityValidator.checkUnique;


/**
 * Service
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class VehicleTypeService {

    @Autowired
    private VehicleTypeDao vehicleTypeDao;

    @Autowired
    private VehicleTypeMapper vehicleTypeMapper;

    @Autowired
    private VehicleModelDao vehicleModelDao;

    @Autowired
    private CmdDebugDao cmdDebugDao;

    public PageQuery<VehicleTypeVM> findAll(VehicleTypeQuery query) {
        PageQuery pageQuery = query.toPageQuery();
        vehicleTypeDao.pageQuery(pageQuery);
        return pageQuery;
    }

    public Optional<VehicleType> get(Long id) {
        return Optional.ofNullable(vehicleTypeDao.single(id));
    }

    public VehicleType create(VehicleTypeDto command) {
        checkNameExist(null, command.getOrganizationId(), command.getName());
        VehicleType entity = vehicleTypeMapper.dtoToEntity(command);
        vehicleTypeDao.insert(entity);
        return entity;
    }

    private void checkNameExist(Long id, Long orgId, String name) {
        VehicleType vehicleTypeCheck = vehicleTypeDao.findByNameAndOrgId(orgId, name);
        checkUnique(Optional.ofNullable(vehicleTypeCheck), id, ErrorConstants.VEHICLE_TYPE_NAME_ALREADY_USED);
    }


    public Optional<VehicleType> update(Long id, VehicleTypeDto command) {
        return get(id).map(entity -> {
            //若类型下面有型号 不可更换组织
            if (!command.getOrganizationId().equals(entity.getOrganizationId())) {
                if (vehicleModelDao.countByVehicleType(id) > 0) {
                    throw new BadRequestException(ErrorConstants.VEHICLE_TYPE_CHANGE_ORG__HAS_RELATION_WITH_VEHICLE_MODEL);
                }
            }
            //校验名称
            checkNameExist(id, command.getOrganizationId(), command.getName());
            entity.setDescription(command.getDescription());
            entity.setName(command.getName());
            entity.setOrganizationId(command.getOrganizationId());
            entity.setUpdateTime(System.currentTimeMillis());
            entity.setUpdateUserAccount(BaseContextHandler.getLoginName());
            entity.setUpdateUserRealname(BaseContextHandler.getName());
            vehicleTypeDao.updateById(entity);
            return entity;
        });
    }

    public void delete(Long id) {
        if (vehicleModelDao.countByVehicleType(id) > 0) {
            throw new BadRequestException(ErrorConstants.VEHICLE_TYPE_HAS_RELATION_WITH_VEHICLE_MODEL);
        }
        if (cmdDebugDao.countByVehicleType(id) > 0) {
            throw new BadRequestException(ErrorConstants.VEHICLE_TYPE_HAS_RELATION_WITH_CMDDEBUG);
        }
        vehicleTypeDao.deleteById(id);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            delete(id);
        }
    }

    public List<OptionVM> vehicleTypeOptionsByOrg(Long rootOrgId) {
        return vehicleTypeDao.vehicleTypeOptionsByOrg(rootOrgId);
    }

    public List<OptionVM> optionsBySelectOrg(Long orgId) {
        return vehicleTypeDao.optionsBySelectOrg(orgId);
    }

    public List<OptionVM> optionsByFinanceId(Long financeId) {
        return vehicleTypeDao.optionsByFinanceId(financeId);
    }

    public List<OptionVM> vehicleTypeOptionsByCurrentUser() {
        if (BaseContextHandler.getUserType().equals(UserType.ADMIN.name())) {
            return vehicleTypeDao.all()
                    .stream()
                    .map(model -> {
                        OptionVM option = new OptionVM();
                        option.setId(model.getId());
                        option.setName(model.getName());
                        return option;
                    }).collect(Collectors.toList());
        }
        if (BaseContextHandler.getUserType().equals(UserType.ORGANIZATION.name())) {
            return vehicleTypeOptionsByOrg(BaseContextHandler.getRootOrgId());
        }
        if (BaseContextHandler.getUserType().equals(UserType.FINANCE.name())) {
            return optionsByFinanceId(BaseContextHandler.getFinanceId());
        }
        return null;
    }
}
