package cn.com.tiza.cmd.command.dao;

import cn.com.tiza.cmd.command.domain.Command;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

/**
 * @author villas
 * gen by beetlsql mapper 2020-05-14
 */
public interface CommandDao extends BaseMapper<Command> {

    void pageQuery(PageQuery<Command> pageQuery);

    /**
     * 获取指定天的最大命令序列号
     *
     * @param day day
     * @return max命令序列号
     */
    Integer getMaxSerialNoByDay(@Param("day") int day);
}
