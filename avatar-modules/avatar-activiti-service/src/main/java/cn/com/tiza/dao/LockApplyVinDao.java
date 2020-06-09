package cn.com.tiza.dao;

import cn.com.tiza.domain.LockApplyVin;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

/**
 * @author villas
 * gen by beetlsql mapper 2020-04-27
 */
public interface LockApplyVinDao extends BaseMapper<LockApplyVin> {
    /**
     * 获取申请单号中的车辆列表
     *
     * @param pageQuery pageQuery
     */
    void pageQuery(PageQuery<LockApplyVin> pageQuery);
}
