package cn.com.tiza.dao;

import cn.com.tiza.domain.SysFunction;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.Collection;
import java.util.List;

/**
 * @author tiza
 */
public interface SysFunctionDao extends BaseMapper<SysFunction> {

    /**
     * 查询用户权限
     * @param userId
     * @return
     */
    List<SysFunction> selectAuthorizesByUser(@Param("userId") Long userId);

    /**
     * 查询角色类型下的可用权限
     * @param roleTypes 角色类型
     * @return list
     */
    default List<SysFunction> selectByRoleType(Collection<Integer> roleTypes) {
        return createLambdaQuery()
                .andIn(SysFunction::getRoleType, roleTypes)
                .select();
    }
}
