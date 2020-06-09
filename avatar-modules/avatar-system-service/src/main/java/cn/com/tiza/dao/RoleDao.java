package cn.com.tiza.dao;

import cn.com.tiza.domain.Role;
import cn.com.tiza.dto.SelectOption;
import cn.com.tiza.web.rest.vm.RoleVM;
import org.apache.commons.lang3.Validate;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;
import org.beetl.sql.core.query.LambdaQuery;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author jiajian
 */
public interface RoleDao extends BaseMapper<Role> {
    /**
     * 分页查询角色列表
     * @param query 查询条件
     * */
    void pageQuery(PageQuery<RoleVM> query);

    /**
     * 获取角色下拉列表信息
     * @param organizationId = role.organizationId
     * @param financeId = role.financeId
     * @return 权限下拉列表信息
     * */
    default List<SelectOption> selectOptions(Long organizationId, Long financeId) {
        Validate.isTrue(Objects.nonNull(organizationId) || Objects.nonNull(financeId), "不能都为null");
        LambdaQuery<Role> query = createLambdaQuery();
        if(Objects.nonNull(organizationId)) {
            query.andEq(Role::getOrganizationId, this.getParentOrgId(organizationId));
        } else {
            query.andEq(Role::getFinanceId, financeId);
        }
        return query.select()
                .stream()
                .map(role -> new SelectOption(role.getId(), role.getRoleName()))
                .collect(Collectors.toList());
    }

    Long getParentOrgId(@Param("orgId") Long orgId);

    /**
     * 查询同组织下的角色
     * @param organizationId = role.organizationId
     * @param financeId = role.financeId
     * @param name = role.name
     * @return Role
     */
    default Optional<Role> findByName(Long organizationId, Long financeId, @NotNull String name) {
        Validate.isTrue(Objects.nonNull(organizationId) || Objects.nonNull(financeId), "不能都为null");

        LambdaQuery<Role> query = createLambdaQuery();
        if(Objects.nonNull(organizationId)) {
            query.andEq(Role::getOrganizationId, organizationId);
        } else {
            query.andEq(Role::getFinanceId, financeId);
        }
        return Optional.ofNullable(query.andEq(Role::getRoleName, name)
                .single());
    }
}
