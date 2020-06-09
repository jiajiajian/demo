package cn.com.tiza.dao;

import cn.com.tiza.domain.User;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author
 */
public interface UserDao extends BaseMapper<User> {

    /**
     * 根据报警/故障 所属组织id查询所有需要系统通知的用户id
     *
     * @param organizationId
     * @return
     */
    List<Long> sysNoticeUserIdsForFaultAndAlarm(@Param("organizationId") Long organizationId);

    /**
     * 查询所有需要系统通知的用户id
     *
     * @param vin
     * @return
     */
    List<Long> sysNoticeUserIdsForFence(@Param("vin") String vin);


    /**
     * 根据组织 角色 查询用户id list
     *
     * @param organizationId
     * @param roleIds
     * @return
     */
    List<Long> userListByRolesAndOrg(@Param("organizationId") Long organizationId,
                                     @Param("roleIds") List<Long> roleIds);


    /**
     * 根据组织 用户id list 查询用户id list(将无效用户过滤)
     *
     *
     * @param organizationId 通知策略组织id
     * @param orgType 报警 组织类型 只有围栏报警有值
     * @param userIds
     * @return
     */
    List<Long> userListByUserIdsAndStrategyType(@Param("organizationId") Long organizationId,
                                     @Param("orgType") Integer orgType,
                                     @Param("userIds") List<Long> userIds);
}
