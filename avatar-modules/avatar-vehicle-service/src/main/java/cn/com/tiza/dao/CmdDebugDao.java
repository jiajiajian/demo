package cn.com.tiza.dao;

import cn.com.tiza.domain.CmdDebug;
import cn.com.tiza.web.rest.vm.CmdDebugVM;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

/**
 * gen by beetlsql mapper 2020-03-10
 */
public interface CmdDebugDao extends BaseMapper<CmdDebug> {
    /**
     * 分页查询
     *
     * @param pageQuery
     */
    void pageQuery(PageQuery<CmdDebugVM> pageQuery);

    /**
     * 查询该根组织配置
     *
     * @param rootOrgId
     * @return
     */
    default CmdDebug findByRootOrg(Long rootOrgId,Long vehicleTypeId) {
        return createLambdaQuery()
                .andEq(CmdDebug::getOrganizationId, rootOrgId)
                .andEq(CmdDebug::getVehicleTypeId,vehicleTypeId)
                .single();
    }

    /**
     * 根据车型查询关联指令配置数量
     *
     * @param vehicleTypeId
     * @return
     */
    default long countByVehicleType(Long vehicleTypeId) {
        return createLambdaQuery()
                .andEq(CmdDebug::getVehicleTypeId, vehicleTypeId)
                .count();
    }
}
