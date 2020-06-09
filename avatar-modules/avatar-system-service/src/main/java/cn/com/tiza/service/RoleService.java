package cn.com.tiza.service;

import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.context.UserInfo;
import cn.com.tiza.dao.AuthorizeDao;
import cn.com.tiza.dao.RoleDao;
import cn.com.tiza.dao.RoleUserDao;
import cn.com.tiza.dao.SysFunctionDao;
import cn.com.tiza.domain.Authorize;
import cn.com.tiza.domain.Role;
import cn.com.tiza.dto.SelectOption;
import cn.com.tiza.service.dto.AuthorizeCommand;
import cn.com.tiza.service.dto.RoleCommand;
import cn.com.tiza.service.dto.RoleQuery;
import cn.com.tiza.service.mapper.RoleMapper;
import cn.com.tiza.util.EntityValidator;
import cn.com.tiza.web.rest.errors.BadRequestException;
import cn.com.tiza.web.rest.errors.ErrorConstants;
import cn.com.tiza.web.rest.errors.SystemErrorConstants;
import cn.com.tiza.web.rest.vm.RoleVM;
import org.apache.commons.lang3.Validate;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author tiza
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleService {
    @Autowired
    private RoleDao roleDao;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private AuthorizeDao authorizeDao;

    @Autowired
    private RoleUserDao roleUserDao;

    @Autowired
    private SysFunctionDao functionDao;

    public PageQuery<RoleVM> findAll(RoleQuery query) {
        PageQuery<RoleVM> pageQuery = query.toPageQuery();
        pageQuery.setOrderBy("t.id DESC");
        roleDao.pageQuery(pageQuery);
        return pageQuery;
    }

    /**
     * 检查角色名称重复
     *
     * @param id   id
     * @param name 名称
     */
    public void checkNameUnique(Long id, String name, Long orgId, Long financeId) {
        Optional<Role> entity = roleDao.findByName(
                orgId, financeId, name);
        EntityValidator.checkUnique(entity, id, SystemErrorConstants.ROLE_NAME_UNIQUE);
    }

    /**
     * @param command 角色command
     **/
    public Role create(RoleCommand command, UserInfo loginUser) {
        // 检查角色名称在同一个组织下唯一
        if (loginUser.isOrganization()) {
            //根级id
            command.setOrganizationId(loginUser.getRootOrgId());
        } else if (loginUser.isFinance()) {
            command.setFinanceId(loginUser.getFinanceId());
        }
        this.checkNameUnique(null, command.getRoleName(), command.getOrganizationId(), command.getFinanceId());
        command.validate();
        Role role = roleMapper.commandToEntity(command);
        roleDao.insert(role, true);
        return role;
    }

    public Optional<Role> get(Long id) {
        return Optional.ofNullable(roleDao.single(id));
    }

    /**
     * @param id:角色id,
     * @param command:角色command
     **/
    public Optional<Role> update(Long id, RoleCommand command, UserInfo loginUser) {
        return get(id).map(role -> {
            if (!Objects.equals(role.getRoleName(), command.getRoleName())) {
                // 检查角色名称在同一个组织下唯一
                this.checkNameUnique(id, command.getRoleName(), role.getOrganizationId(), role.getFinanceId());
            }
            //只能改自己机构的角色
            if (loginUser.isFinance()) {
                Validate.isTrue(Objects.equals(loginUser.getFinanceId(), role.getFinanceId()));
            } else if (loginUser.isOrganization()) {
                Validate.isTrue(Objects.equals(loginUser.getRootOrgId(), role.getOrganizationId()));
            }
            role.setRoleName(command.getRoleName());
            role.setRemark(command.getRemark());
            role.setUpdateInfo();
            roleDao.updateById(role);
            return role;
        });
    }

    /**
     * @param ids :id数组
     **/
    public void delete(List<Long> ids) {
        ids.forEach(id -> Optional.ofNullable(roleDao.single(id))
                .ifPresent(role -> {
                    if (roleUserDao.countByRoleId(id) > 0) {
                        throw new BadRequestException(ErrorConstants.ROLE_HAS_RELATION_WITH_USER_TYPE);
                    }
                    roleDao.deleteById(id);
                }));
    }

    /**
     * 角色授权
     *
     * @param roleId   roleId
     * @param commands permissions
     */
    public void updateAuthorizes(Long roleId, List<AuthorizeCommand> commands) {
        authorizeDao.deleteByRoleId(roleId);
        //重新插入
        commands.forEach(command -> {
            Authorize authorize = new Authorize();
            authorize.setAuthorizeObjectId(roleId);
            authorize.setPermissionId(command.getId());
            authorize.setCheckStatus(command.getCheckStatus());
            authorize.setAuthorizeObjectType(0);
            authorize.setPermissionType(3);
            authorize.setAuthorizeTime(System.currentTimeMillis());
            authorize.setAuthorizeAccount(BaseContextHandler.getLoginName());
            authorizeDao.insertTemplate(authorize);
        });
    }

    public List<SelectOption> selectOptions(Long organizationId, Long financeId) {
        return roleDao.selectOptions(organizationId, financeId);
    }

    public List<SelectOption> optionsByPresentUser() {
        return roleDao.createLambdaQuery()
                .andEq(Role::getOrganizationId, BaseContextHandler.getRootOrgId())
                .select()
                .stream()
                .map(role -> new SelectOption(role.getId(), role.getRoleName()))
                .collect(Collectors.toList());
    }

}
