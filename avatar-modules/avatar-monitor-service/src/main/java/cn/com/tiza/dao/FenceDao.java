package cn.com.tiza.dao;

import cn.com.tiza.domain.Fence;
import cn.com.tiza.web.rest.vm.FenceVM;
import cn.com.tiza.web.rest.vm.VehicleVM;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

/**
 * gen by beetlsql mapper 2020-03-31
 */
public interface FenceDao extends BaseMapper<Fence> {
    /**
     * 分页
     *
     * @param pageQuery
     */
    void pageQuery(PageQuery<FenceVM> pageQuery);

    /**
     * 未关联围栏车辆
     *
     * @param pageQuery
     */
    void pageQueryUnRelatedVehicles(PageQuery<VehicleVM> pageQuery);

    /**
     * 已关联围栏车辆
     *
     * @param pageQuery
     */
    void pageQueryRelatedVehicles(PageQuery<VehicleVM> pageQuery);

}
