package cn.com.tiza.service;

import cn.com.tiza.constant.Constants;
import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.context.UserInfo;
import cn.com.tiza.dao.*;
import cn.com.tiza.domain.Organization;
import cn.com.tiza.dto.UserType;
import cn.com.tiza.service.dto.OrganizationCommand;
import cn.com.tiza.service.mapper.OrganizationMapper;
import cn.com.tiza.util.EntityValidator;
import cn.com.tiza.web.client.VehicleApiClient;
import cn.com.tiza.web.rest.errors.BadRequestException;
import cn.com.tiza.web.rest.errors.SystemErrorConstants;
import cn.com.tiza.web.rest.vm.OrganizationVM;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tiza
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class OrganizationService {

    @Autowired
    private OrganizationDao organizationDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private VehicleApiClient vehicleApiClient;

    @Autowired
    private FinanceOrganizationDao financeOrganizationDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private VehicleDao vehicleDao;

    @Autowired
    private AlarmItemDao alarmItemDao;

    @Autowired
    private FaultDictDao faultDictDao;

    @Autowired
    private FenceDao fenceDao;

    private List<OrganizationVM> getOrgChild(Long orgId) {
        return organizationDao.getOrgChild(orgId);
    }

    public List<Organization> getChild(Long orgId) {
        return organizationDao.getChild(orgId);
    }

    public List<Organization> rootList() {
        List<Organization> list = Lists.newArrayList();
        if (BaseContextHandler.getUserType().equals(UserType.ADMIN.name())) {
            list = organizationDao.rootList();
        } else if (BaseContextHandler.getUserType().equals(UserType.ORGANIZATION.name())) {
            list = organizationDao.createLambdaQuery().andEq(Organization::getId, BaseContextHandler.getRootOrgId()).select();
        }
        return list;
    }

    public void checkNameUnique(Long id, Long parentId, String name) {
        Optional<Organization> organization = organizationDao.findByParentIdAndName(parentId, name);
        EntityValidator.checkUnique(organization, id, SystemErrorConstants.ORGANIZATION_NAME_UNIQUE);
    }

    /**
     * 创建新机构
     *
     * @param command command
     */
    public Organization create(OrganizationCommand command) {
        Organization org = organizationMapper.commandToEntity(command);
        organizationDao.insert(org, true);
        updateOrgPath(org);
        organizationDao.updateById(org);
        return org;
    }

    /**
     * 更新机构
     */
    public Optional<Organization> update(Long id, OrganizationCommand command, UserInfo loginUser) {
        return organizationDao.get(id).map(organization -> {
            if (!Objects.equals(organization.getOrgName(), command.getOrgName())) {
                // 检查组织id在同一个父组织下唯一
                this.checkNameUnique(id, command.getParentOrgId(), command.getOrgName());
                organization.setOrgName(command.getOrgName());
            }
            //不能修改自己的父组织
            if (!Objects.equals(loginUser.getOrgId(), id)
                    && !Objects.equals(organization.getParentOrgId(), command.getParentOrgId())) {
                organization.setParentOrgId(command.getParentOrgId());
                updateOrgPath(organization);
            }
            organization.setOrgCode(command.getOrgCode());
            organization.setAbbrName(command.getAbbrName());
            organization.setOrgTypeId(command.getOrgTypeId());
            organization.setContactName(command.getContactName());
            organization.setFaxNo(command.getFaxNo());
            organization.setTelephoneNumber(command.getTelephoneNumber());
            organization.setEmailAddress(command.getEmailAddress());
            organization.setProvinceName(command.getProvinceName());
            organization.setCityName(command.getCityName());
            organization.setCountyName(command.getCountyName());
            organization.setContactAddress(command.getContactAddress());
            organization.setRemark(command.getRemark());
            organization.setUpdateInfo();
            organizationDao.updateById(organization);
            return organization;
        });

    }


    /**
     * 根据上级设置PATH
     *
     * @param org
     */
    private void updateOrgPath(Organization org) {
        Organization parent = null;
        if (org.hasParent()) {
            parent = organizationDao.unique(org.getParentOrgId());
            org.setRootOrgId(parent.getRootOrgId());
        } else {
            org.setRootOrgId(org.getId());
        }
        org.setPath(org.genPath(parent));
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        UserInfo loginUser = BaseContextHandler.getUser();
        Validate.isTrue(!loginUser.isFinance());
        for (Long id : ids) {
            //不能删除自已所在组织
            if (loginUser.isOrganization() && id.equals(loginUser.getOrgId())) {
                throw new BadRequestException("error.organization.delete.self_org");
            }
            organizationDao.get(id).ifPresent(org -> {
                //判断有权限
                log.debug("权限判断{}:{}:{}",loginUser.isOrganization(),org.getPath(),loginUser.getOrgId());
                if (loginUser.isOrganization() && !org.isParent(loginUser.getOrgId())) {
                    throw new BadRequestException("error.organization.delete.no_permission");
                }
                //关联了融资机构
                if (financeOrganizationDao.countByOrgId(id) > 0) {
                    throw new BadRequestException("error.organization.delete.has_finance");
                }
                //有下级
                if (organizationDao.countChildren(id) > 0) {
                    throw new BadRequestException("error.organization.delete.has_children");
                }
                //有用户
                if (userDao.countByOrgId(id) > 0) {
                    throw new BadRequestException("error.organization.delete.has_user");
                }
                if (org.isRoot()) {
                    //关联故障字典
                    if(faultDictDao.countByOrgId(id) > 0){
                        throw new BadRequestException("error.organization.delete.has_fault_dic");
                    }
                    //关联围栏策略
                    if(fenceDao.countByOrgId(id) > 0){
                        throw new BadRequestException("error.organization.delete.has_fence");
                    }
                    //有客户
                    if (customerDao.countByOrgId(id) > 0) {
                        throw new BadRequestException("error.organization.delete.has_customer");
                    }
                    //有车辆
                    if (vehicleDao.countByOrgId(id) > 0) {
                        throw new BadRequestException("error.organization.delete.has_vehicle");
                    }
                    //有报警信息
                    if (alarmItemDao.countByOrgId(id) > 0) {
                        throw new BadRequestException("error.organization.delete.has_alarm");
                    }
                }
                organizationDao.deleteById(id);
            });
        }
    }

    /**
     * 通过id获取org
     */
    public Optional<OrganizationVM> get(Long id) {
        return organizationDao.get(id).map(organizationMapper::toVM).map(org -> {
            if (org.getParentOrgId() != null && !Constants.ROOT_ORG_ID.equals(org.getParentOrgId())) {
                organizationDao.get(org.getParentOrgId())
                        .ifPresent(parent -> org.setParentOrgName(parent.getOrgName()));
            }
            return org;
        });
    }

    /**
     * 查询组织列表
     *
     * @param name      过滤 名称
     * @param loginUser 登录名
     * @return org list
     */
    public List<OrganizationVM> getOrgList(String name, UserInfo loginUser) {
        List<OrganizationVM> lists;
        Long rootId = Constants.ROOT_ORG_ID;
        //存储name/code like 的组织
        List<OrganizationVM> filterList = null;
        if (loginUser.isAdmin()) {
            lists = organizationDao.findAll();
        } else if (loginUser.isOrganization()) {
            Long orgId = loginUser.getOrgId();
            lists = getOrgChild(orgId);
            rootId = organizationDao.single(orgId).getParentOrgId();
        } else {
            //融资机构组织树: 所有关联一级机构及其子机构构成的树
            lists = Lists.newArrayList();
            List<Long> rootOrgIds = financeOrganizationDao.getOrgIdsByFinanceId(loginUser.getFinanceId());
            for (int i = 0; i < rootOrgIds.size(); i++) {
                List<OrganizationVM> orgChild = getOrgChild(rootOrgIds.get(i));
                lists.addAll(orgChild);
            }
        }
        if (lists.isEmpty()) {
            return Collections.emptyList();
        }
        if (StringUtils.hasText(name)) {
            final String likeName = name.trim();
            filterList = lists.stream()
                    .filter(org -> org.getOrgName().contains(likeName)
                            || (org.getOrgCode() != null && org.getOrgCode().contains(likeName)))
                    .collect(Collectors.toList());
            //没有匹配的数据
            if (filterList.isEmpty()) {
                return Collections.emptyList();
            }
            //过滤出id来
            Set<Long> filterIds = new HashSet<>();
            for (OrganizationVM org : filterList) {
                filterIds.addAll(parseOrgPath(org.getPath(), rootId));
            }
            lists = lists.stream().filter(org -> filterIds.contains(org.getId())).collect(Collectors.toList());
        }

        return organizationMapper.buildOrgTreeTable(lists, rootId);
    }

    private List<Long> parseOrgPath(String path, Long root) {
        List<Long> ids = new ArrayList<>();
        String[] idArr = path.split("/");
        boolean isRoot = Constants.ROOT_ORG_ID.equals(root);
        for (String id : idArr) {
            if (isRoot) {
                ids.add(Long.parseLong(id));
            } else {
                Long x = Long.parseLong(id);
                if (x.longValue() >= root.longValue()) {
                    ids.add(x);
                }
            }
        }
        return ids;
    }

    public List<Organization> rootOrgOptionsByUserType() {
        List<Organization> organizations = Lists.newArrayList();
        String userType = BaseContextHandler.getUserType();
        if (UserType.ADMIN.name().equals(userType)) {
            organizations = organizationDao.rootList();
        } else {
            Long rootOrgId = BaseContextHandler.getRootOrgId();
            Organization organization = organizationDao.single(rootOrgId);
            organizations.add(organization);
        }
        return organizations;
    }

    public List<Organization> getChildrenOrgsByUserType() {
        if (BaseContextHandler.getUserType().equals(UserType.ADMIN.name())) {
            return organizationDao.all();
        } else {
            return organizationDao.getChild(BaseContextHandler.getOrgId());
        }
    }
}
