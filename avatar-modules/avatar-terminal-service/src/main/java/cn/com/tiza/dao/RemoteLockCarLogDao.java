package cn.com.tiza.dao;

import cn.com.tiza.domain.Lock;
import cn.com.tiza.domain.RemoteLockCarLog;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;

/**
* 
* gen by beetlsql mapper 2020-03-21
 * @author xiaotao
 */
public interface RemoteLockCarLogDao extends BaseMapper<RemoteLockCarLog> {

    /**
     * 锁车日志分页查询
     * @param pageQuery
     */
    void pageQuery(PageQuery<Lock> pageQuery);

}
