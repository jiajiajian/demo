package cn.com.tiza.dao;

import cn.com.tiza.domain.Log;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

/**
 * @author 0837
 */
public interface LogDao extends BaseMapper<Log> {
    /**
     * 获取用户信息
     * @param query 查询条件
     */
    void getLogInfo(PageQuery<Log> query);
}
