package cn.com.tiza.dao;

import cn.com.tiza.domain.SoftVersion;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

/*
 *
 * gen by beetlsql mapper 2020-03-09
 */
public interface SoftVersionDao extends BaseMapper<SoftVersion> {

    void pageQuery(PageQuery pageQuery);
}
