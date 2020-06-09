package cn.com.tiza.service;

import cn.com.tiza.dao.FinanceDao;
import cn.com.tiza.dao.FinanceOrganizationDao;
import cn.com.tiza.dao.OrganizationDao;
import cn.com.tiza.dao.UserDao;
import cn.com.tiza.domain.Finance;
import cn.com.tiza.domain.FinanceOrganization;
import cn.com.tiza.dto.SelectOption;
import cn.com.tiza.service.dto.FinanceDto;
import cn.com.tiza.service.dto.FinanceQuery;
import cn.com.tiza.service.mapper.FinanceMapper;
import cn.com.tiza.util.EntityValidator;
import cn.com.tiza.web.rest.errors.BadRequestException;
import cn.com.tiza.web.rest.errors.SystemErrorConstants;
import cn.com.tiza.web.rest.vm.FinanceVM;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 融资机构 Service
 * gen by beetlsql 2020-03-06
 *
 * @author tiza
 */
@Slf4j
@Service
public class FinanceService {

    @Autowired
    private FinanceDao financeDao;

    @Autowired
    private FinanceOrganizationDao financeOrganizationDao;

    @Autowired
    private OrganizationDao organizationDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private FinanceMapper financeMapper;

    public PageQuery<FinanceVM> findAll(FinanceQuery query) {
        PageQuery<FinanceVM> pageQuery = query.toPageQuery();
        pageQuery.setOrderBy("t.id desc");
        financeDao.pageQuery(pageQuery);
        return pageQuery;
    }

    public List<FinanceVM> exportQuery(FinanceQuery query) {
        return financeDao.exportQuery(query.params());
    }

    public Optional<FinanceVM> getFinance(Long id) {
        return Optional.ofNullable(financeDao.single(id))
                .map(obj -> {
                    obj.setOrgIds(financeOrganizationDao.getOrgIdsByFinanceId(id));
                    return financeMapper.toVM(obj);
                });
    }

    public Optional<Finance> get(Long id) {
        return Optional.ofNullable(financeDao.single(id));
    }

    public void checkNameUnique(Long id, String name) {
        Optional<Finance> obj = financeDao.findByName(name);
        EntityValidator.checkUnique(obj, id, SystemErrorConstants.FINANCE_NAME_UNIQUE);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public Finance create(FinanceDto command) {
        Finance entity = financeMapper.dtoToEntity(command);
        financeDao.insert(entity, true);

        addFinanceOrganization(entity.getId(), command.getOrgIds());
        return entity;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public Optional<Finance> update(Long id, FinanceDto command) {
        return get(id).map(entity -> {
            if(! Objects.equals(entity.getName(), command.getName())) {
                this.checkNameUnique(id, command.getName());
                entity.setName(command.getName());
            }
            entity.setContactAddress(command.getContactAddress());
            entity.setContactName(command.getContactName());
            entity.setEmailAddress(command.getEmailAddress());
            entity.setFaxNo(command.getFaxNo());
            entity.setRemark(command.getRemark());
            entity.setTelephoneNumber(command.getTelephoneNumber());
            entity.setUpdateInfo();

            financeDao.updateById(entity);

            financeOrganizationDao.deleteByFinanceId(id);
            addFinanceOrganization(entity.getId(), command.getOrgIds());
            return entity;
        });
    }

    private void addFinanceOrganization(Long financeId, List<Long> orgIds) {
        if (orgIds != null && !orgIds.isEmpty()) {
            for (Long orgId : orgIds) {
                //只有根才能和融资机构关联
                Optional.ofNullable(organizationDao.single(orgId))
                        .filter(org -> org.isRoot())
                        .ifPresent(org ->
                                financeOrganizationDao.insert(new FinanceOrganization(financeId, orgId)));
                ;
            }
        }
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void delete(Long id) {
        //check user
        if(userDao.countByFinanceId(id) > 0) {
            throw new BadRequestException("error.finance.delete.has_user");
        }
        //TODO check vehicles
        financeOrganizationDao.deleteByFinanceId(id);
        financeDao.deleteById(id);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void delete(Long[] ids) {
        for (Long id : ids) {
            delete(id);
        }
    }

    public List<SelectOption> findOptionsByOrgId(Long organizationId){
        return financeDao.findOptionsByOrgId(organizationId);
    }
}
