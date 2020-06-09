package cn.com.tiza.dao;

import cn.com.tiza.domain.AlarmItem;
import org.beetl.sql.core.mapper.BaseMapper;

public interface AlarmItemDao extends BaseMapper<AlarmItem> {

    /**
     * 根据组织查询报警项配置
     *
     * @param orgId
     * @return
     */
    default Long countByOrgId(Long orgId) {
        return createLambdaQuery()
                .andEq(AlarmItem::getOrganizationId, orgId)
                .count();
    }

}
