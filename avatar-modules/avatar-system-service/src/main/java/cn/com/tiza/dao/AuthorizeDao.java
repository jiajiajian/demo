package cn.com.tiza.dao;

import cn.com.tiza.domain.Authorize;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author tiza
 */
public interface AuthorizeDao extends BaseMapper<Authorize> {

    /**
     * 删除角色所有授权数据
     * @param roleId
     */
    void deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 角色所有授权数据
     * @param roleId
     * @return
     */
    List<Long> selectByRoleId(@Param("roleId") Long roleId);
}
