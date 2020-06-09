package cn.com.tiza.dao;

import cn.com.tiza.domain.FunctionSetItemLock;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;

/**
 * gen by beetlsql mapper 2020-03-31
 */
public interface FunctionSetItemLockDao extends BaseMapper<FunctionSetItemLock> {

    /**
     * 分页查询
     *
     * @param pageQuery pageQuery
     */
    void pageQuery(PageQuery<FunctionSetItemLock> pageQuery);

    /**
     * 根据终端编号查询出锁车项
     *
     * @param terminalCode terminalCode
     * @return 锁车项
     */
    List<FunctionSetItemLock> getLockOptions(@Param("terminalCode") String terminalCode);
}
