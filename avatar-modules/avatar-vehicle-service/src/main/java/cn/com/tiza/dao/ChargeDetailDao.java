package cn.com.tiza.dao;

import cn.com.tiza.domain.ChargeDetail;
import cn.com.tiza.web.rest.vm.ChargeDetailVM;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

/**
 *
 * gen by beetlsql mapper 2020-04-20
 */
public interface ChargeDetailDao extends BaseMapper<ChargeDetail> {
    void pageQuery(PageQuery<ChargeDetailVM> pageQuery);
}
