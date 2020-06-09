package cn.com.tiza.dao;

import cn.com.tiza.domain.RoleUser;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author tiza
 */
public interface RoleUserDao extends BaseMapper<RoleUser> {

    /**
     * 删除 角色用户关联关系
     * @param userId user.id
     * @return num
     */
    default int deleteByUserId(Long userId) {
        return createLambdaQuery()
                .andEq(RoleUser::getUserId, userId)
                .delete();
    }

    /**
     * 删除角色所有角色用户关联关系
     *
     * @param roleId 角色id
     * @return number
     */
    int deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 查询角色所有角色用户关联关系
     *
     * @param roleId 用户id
     * @return
     */
    List<RoleUser> selectByRoleId(@Param("roleId") Long roleId);

    /**
     * 查询用户所有角色用户关联关系
     *
     * @param userId 用户id
     * @return RoleUser
     */
    default List<RoleUser> selectByUserId(Long userId) {
        return createLambdaQuery()
                .andEq(RoleUser::getUserId, userId)
                .select();
    }

    /**
     * 查询用户所属角色名称
     * @param userId
     * @return
     */
    List<String> findUserRoleName(@Param("userId") Long userId);

    /**
     * 查询角色关联用户数
     *
     * @param roleId
     * @return
     */
    default long countByRoleId(Long roleId) {
        return createLambdaQuery()
                .andEq(RoleUser::getRoleId, roleId)
                .count();
    }

}
