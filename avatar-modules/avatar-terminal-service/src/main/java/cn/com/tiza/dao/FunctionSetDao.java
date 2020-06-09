package cn.com.tiza.dao;

import cn.com.tiza.domain.FunctionSet;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;


public interface FunctionSetDao extends BaseMapper<FunctionSet> {

    void pageQuery(PageQuery pageQuery);
}
