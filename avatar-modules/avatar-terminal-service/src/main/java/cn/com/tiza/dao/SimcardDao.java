package cn.com.tiza.dao;

import cn.com.tiza.domain.Simcard;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

/*
 *
 * gen by beetlsql mapper 2020-03-09
 */
public interface SimcardDao extends BaseMapper<Simcard> {

    void pageQuery(PageQuery pageQuery);
}
