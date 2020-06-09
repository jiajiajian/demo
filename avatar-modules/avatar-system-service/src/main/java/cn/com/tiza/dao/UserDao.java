package cn.com.tiza.dao;

import cn.com.tiza.domain.User;
import cn.com.tiza.dto.SelectOption;
import cn.com.tiza.service.dto.UserQuery;
import cn.com.tiza.web.rest.vm.UserVM;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author 0837
 */
public interface UserDao extends BaseMapper<User> {

    /**
     * 根据查询条件查询用户信息
     * @param query 查询条件
     */
    void pageQuery(PageQuery<UserVM> query);

    /**
     * 导出查询
     *
     * @param paras params
     * @return list
     */
    List<UserVM> exportQuery(Map<String, Object> paras);

    /**
     * 根据用户ID角色
     * @param userId 用户ID
     * @param orgId 机构ID
     * @return 角色名称列表
     */
    List<String> getRolesByUserId(@Param("userId") Long userId, @Param("orgId")Long orgId);

    /**
     * 根据用户ID获取角色ID列表
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<Long> getRoleIdByUserId(@Param("userId") Long userId);

    /**
     * 查询用户
     * @param loginName 用户名
     * @return
     */
    default Optional<User> findByLoginName(String loginName) {
        return Optional.ofNullable(createLambdaQuery()
                .andEq(User::getLoginName, loginName)
                .andEq(User::getDelFlag, false)
                .single()) ;
    }

    /**
     * 查询用户的机构id
     * @param loginName 用户名
     * @return
     */
    default Long getOrgIdByLoginName(String loginName) {
        return findByLoginName(loginName)
                .map(User::getOrganizationId)
                .orElse(null);
    }

    /**
     * 查询机构用户数
     * @param orgId 机构
     * @return num
     */
    default Long countByOrgId(Long orgId) {
        return createLambdaQuery().andEq(User::getOrganizationId, orgId)
                .andEq(User::getDelFlag, false)
                .count();
    }

    /**
     * 查询融资机构用户数
     * @param financeId 融资机构
     * @return num
     */
    default Long countByFinanceId(Long financeId) {
        return createLambdaQuery().andEq(User::getFinanceId, financeId)
                .andEq(User::getDelFlag, false)
                .count();
    }

    /**
     * 根据机构查询用户下拉选
     * @param organizationId
     * @return num
     */
    List<SelectOption> optionsByOrgId(@Param("organizationId") Long organizationId);
}
