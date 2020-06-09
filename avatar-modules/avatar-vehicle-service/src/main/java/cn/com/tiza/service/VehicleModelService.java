package cn.com.tiza.service;


import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.dao.CmdDebugDao;
import cn.com.tiza.dao.VehicleDao;
import cn.com.tiza.dao.VehicleModelDao;
import cn.com.tiza.domain.VehicleModel;
import cn.com.tiza.dto.UserType;
import cn.com.tiza.service.dto.VehicleModelDto;
import cn.com.tiza.service.dto.VehicleModelQuery;
import cn.com.tiza.service.mapper.VehicleModelMapper;
import cn.com.tiza.util.EntityValidator;
import cn.com.tiza.web.rest.errors.BadRequestException;
import cn.com.tiza.web.rest.errors.ErrorConstants;
import cn.com.tiza.web.rest.vm.OptionVM;
import cn.com.tiza.web.rest.vm.VehicleModelVM;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Slf4j
@Service
@Transactional
public class VehicleModelService {

    @Autowired
    private VehicleModelDao vehicleModelDao;

    @Autowired
    private VehicleModelMapper vehicleModelMapper;

    @Autowired
    private VehicleTypeService vehicleTypeService;

    @Autowired
    private VehicleDao vehicleDao;

    @Autowired
    private CmdDebugDao cmdDebugDao;

    public PageQuery<VehicleModelVM> findAll(VehicleModelQuery query) {
        PageQuery<VehicleModelVM> pageQuery = query.toPageQuery();
        vehicleModelDao.pageQuery(pageQuery);
        return pageQuery;
    }

    public Optional<VehicleModel> get(Long id) {
        return Optional.ofNullable(vehicleModelDao.single(id));
    }

    public VehicleModel create(VehicleModelDto command) {
        checkNameExist(null, command.getOrganizationId(), command.getName());
        checkVehicleType(command.getVehicleTypeId());
        VehicleModel entity = vehicleModelMapper.dtoToEntity(command);
        vehicleModelDao.insert(entity);
        return entity;
    }

    private void checkNameExist(Long id, Long orgId, String name) {
        VehicleModel vehicleModeCheck = vehicleModelDao.findByNameAndOrgId(orgId, name);
        EntityValidator.checkUnique(Optional.ofNullable(vehicleModeCheck), id, ErrorConstants.VEHICLE_MODEL_NAME_ALREADY_USED);
    }

    private void checkVehicleType(Long vehicleTypeId) {
        vehicleTypeService.get(vehicleTypeId)
                .orElseThrow(() -> new BadRequestException(ErrorConstants.VEHICLE_TYPE_NOT_EXIST));
    }

    public Optional<VehicleModel> update(Long id, VehicleModelDto command) {
        return get(id).map(entity -> {
            checkNameExist(id, command.getOrganizationId(), command.getName());
            checkVehicleType(command.getVehicleTypeId());
            entity.setName(command.getName());
            entity.setTonnage(command.getTonnage());
            entity.setOrganizationId(command.getOrganizationId());
            entity.setVehicleTypeId(command.getVehicleTypeId());
            entity.setDescription(command.getDescription());
            entity.setUpdateTime(System.currentTimeMillis());
            entity.setUpdateUserAccount(BaseContextHandler.getLoginName());
            entity.setUpdateUserRealname(BaseContextHandler.getName());
            vehicleModelDao.updateById(entity);
            return entity;
        });
    }

    public void delete(Long id) {
        if (vehicleDao.countByVehicleModel(id) > 0) {
            throw new BadRequestException(ErrorConstants.VEHICLE_MODEL_HAS_RELATION_WITH_VEHICLE);
        }

        vehicleModelDao.deleteById(id);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            delete(id);
        }
    }

    public List<OptionVM> vehicleModelOptionsByOrg(Long orgId) {
        return vehicleModelDao.vehicleModelOptionsByOrg(orgId);
    }

    public List<OptionVM> vehicleModelOptionsByCurrentUser() {
        if (BaseContextHandler.getUserType().equals(UserType.ADMIN.name())) {
            return vehicleModelDao.all()
                    .stream()
                    .map(model -> {
                        OptionVM option = new OptionVM();
                        option.setId(model.getId());
                        option.setName(model.getName());
                        return option;
                    }).collect(Collectors.toList());
        }
        if (BaseContextHandler.getUserType().equals(UserType.ORGANIZATION.name())) {
            return vehicleModelOptionsByOrg(BaseContextHandler.getRootOrgId());
        }
        if (BaseContextHandler.getUserType().equals(UserType.FINANCE.name())) {
            return optionsByFinanceId(BaseContextHandler.getFinanceId());
        }
        return null;
    }


    public List<OptionVM> vehicleModelOptionsVehicleType(Long vehicleType) {
        return vehicleModelDao.vehicleModelOptionsVehicleType(vehicleType);
    }

    public List<OptionVM> optionsByFinanceId(Long financeId) {
        return vehicleModelDao.optionsByFinanceId(financeId);
    }
}
