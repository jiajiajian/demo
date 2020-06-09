package cn.com.tiza.dao;

import cn.com.tiza.domain.NoticeStrategy;
import cn.com.tiza.dto.SelectOption;
import cn.com.tiza.service.dto.NoticeStrategyQuery;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;

/**
 * gen by beetlsql mapper 2020-03-23
 */
public interface NoticeStrategyDao extends BaseMapper<NoticeStrategy> {

    /**
     * 获取通知策略
     *
     * @param query
     * @return
     */
    default List<NoticeStrategy> strategies(NoticeStrategyQuery query) {
        return createLambdaQuery()
                .andEq(NoticeStrategy::getOrgType, query.getOrgType())
                .andEq(NoticeStrategy::getOrganizationId, query.getOrganizationId())
                .select();
    }

    /**
     * 获取用户list
     *
     * @param userIdArr
     * @return
     */
    List<SelectOption> findUserListByIds(@Param("userIdArr") List<Long> userIdArr);

    /**
     * 获取角色list
     *
     * @param roleIdArr
     * @return
     */
    List<SelectOption> findRoleListByIds(@Param("roleIdArr") List<Long> roleIdArr);

    /**
     * 用户下拉选
     *
     * @param userType
     * @param organizationId
     * @return
     */
    List<SelectOption> userOptions(@Param("userType") Integer userType,
                                   @Param("organizationId") Long organizationId);

    /**
     * 角色下拉选
     *
     * @param roleType
     * @param organizationId
     * @param financeId
     * @return
     */
    List<SelectOption> roleOptions(@Param("roleType") Integer roleType,
                                   @Param("organizationId") Long organizationId,
                                   @Param("financeId") Long financeId);

    /**
     * 查找 报警/故障 自己和所有父级组织通知策略
     *
     * @param orgs
     * @param alarmType      FAULT OR ALARM
     * @return
     */
    List<NoticeStrategy> findStrategiesForFaultOrAlarm(@Param("orgs") String[] orgs,
                                                       @Param("alarmType") String alarmType);

    /**
     * 查找 围栏策略
     *
     * @param organizationId
     * @param orgType        1：普通机构 2 融资机构
     * @return
     */
    List<NoticeStrategy> findStrategiesForFence(@Param("organizationId") Long organizationId,
                                                @Param("orgType") Integer orgType);


    /**
     * 获取组织路径
     * @param orgId
     * @return
     */
    String orgPath(@Param("orgId") Long orgId);

    /**
     * 获取报警项名称
     * @param code
     * @return
     */
    String alarmItemName(@Param("code") String code);
}
