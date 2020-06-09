package cn.com.tiza.dao;

import cn.com.tiza.domain.Vehicle;
import org.beetl.sql.core.mapper.BaseMapper;

public interface VehicleDao extends BaseMapper<Vehicle> {

    /**
     * 根据组织查询报警项配置
     *
     * @param orgId
     * @return
     */
    default Long countByOrgId(Long orgId) {
        return createLambdaQuery()
                .andEq(Vehicle::getOrganizationId, orgId)
                .count();
    }

}
