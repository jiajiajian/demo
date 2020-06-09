package cn.com.tiza.service;

import cn.com.tiza.context.UserInfo;
import cn.com.tiza.dao.CustomerDao;
import cn.com.tiza.dao.OrganizationDao;
import cn.com.tiza.domain.Customer;
import cn.com.tiza.domain.Organization;
import cn.com.tiza.dto.SelectOption;
import cn.com.tiza.service.dto.CustomerDto;
import cn.com.tiza.service.dto.CustomerQuery;
import cn.com.tiza.service.mapper.CustomerMapper;
import cn.com.tiza.util.EntityValidator;
import cn.com.tiza.web.rest.errors.BadRequestException;
import cn.com.tiza.web.rest.errors.SystemErrorConstants;
import cn.com.tiza.web.rest.vm.CustomerVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Service
 * gen by beetlsql 2020-03-06
 *
 * @author tiza
 */
@Slf4j
@Service
public class CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private OrganizationDao organizationDao;

    public PageQuery<CustomerVO> findAll(CustomerQuery query) {
        PageQuery<CustomerVO> pageQuery = query.toPageQuery();
        pageQuery.setOrderBy("t.id desc");
        customerDao.pageQuery(pageQuery);
        return pageQuery;
    }

    public List<CustomerVO> exportQuery(CustomerQuery query) {
        return customerDao.exportQuery(query.params());
    }

    public Optional<Customer> get(Long id) {
        return Optional.ofNullable(customerDao.single(id));
    }

    public List<SelectOption> optionsByOrgId(Long orgId) {
        return customerDao.optionsByOrgId(orgId);
    }

    public List<SelectOption> optionsBySelectOrg(Long orgId) {
        Organization organization = organizationDao.single(orgId);
        return Optional.ofNullable(organization)
                .map(o -> customerDao.optionsByOrgId(o.getRootOrgId()))
                .orElseGet(ArrayList::new);
    }

    public void checkNameUnique(Long id, Long organizationId, String name) {
        Optional<Customer> obj = customerDao.findByName(organizationId, name);
        EntityValidator.checkUnique(obj, id, SystemErrorConstants.FINANCE_NAME_UNIQUE);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public Customer create(CustomerDto command, UserInfo loginUser) {
        if (loginUser.isOrganization()) {
            command.setOrganizationId(loginUser.getRootOrgId());
        }
        Validate.notNull(command.getOrganizationId());
        checkNameUnique(null, command.getOrganizationId(), command.getName());
        Customer entity = customerMapper.dtoToEntity(command);
        customerDao.insert(entity);
        return entity;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public Optional<Customer> update(Long id, CustomerDto command, UserInfo loginUser) {
        return get(id).map(entity -> {
            if (loginUser.isOrganization() && !Objects.equals(loginUser.getRootOrgId(), entity.getOrganizationId())) {
                throw new BadRequestException("不能修改！");
            }
            if (!Objects.equals(entity.getName(), command.getName())) {
                checkNameUnique(id, entity.getOrganizationId(), command.getName());
            }
            entity.setName(command.getName());
            entity.setAlarmName(command.getAlarmName());
            entity.setAlarmNumber(command.getAlarmNumber());
            entity.setOwnerName(command.getOwnerName());
            entity.setOwnerNumber(command.getOwnerNumber());
            entity.setPhoneNumber(command.getPhoneNumber());
            entity.setOrganizationId(command.getOrganizationId());
            entity.setRemark(command.getRemark());
            entity.setUpdateInfo();
            customerDao.updateTemplateById(entity);
            return entity;
        });
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void delete(Long id) {
        //检查被车辆引用
        customerDao.deleteById(id);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void delete(Long[] ids) {
        for (Long id : ids) {
            delete(id);
        }
    }
}
