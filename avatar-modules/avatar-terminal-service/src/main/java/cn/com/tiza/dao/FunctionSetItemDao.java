package cn.com.tiza.dao;

import cn.com.tiza.domain.FunctionSetItem;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

/**
 * @author villas
 * gen by beetlsql mapper 2020-03-09
 */
public interface FunctionSetItemDao extends BaseMapper<FunctionSetItem> {

    void pageQuery(PageQuery pageQuery);
}
