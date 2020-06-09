package cn.com.tiza.dao;

import cn.com.tiza.domain.FenceVehicle;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

/**
 * gen by beetlsql mapper 2020-03-31
 */
public interface FenceVehicleDao extends BaseMapper<FenceVehicle> {
    void pageQuery(PageQuery pageQuery);
}
