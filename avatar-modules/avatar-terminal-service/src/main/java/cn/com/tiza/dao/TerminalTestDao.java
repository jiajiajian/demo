package cn.com.tiza.dao;

import cn.com.tiza.domain.TerminalTest;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;

/**
 * gen by beetlsql mapper 2020-04-16
 */
public interface TerminalTestDao extends BaseMapper<TerminalTest> {

    /**
     * 查询指定终端的检测指令记录
     *
     * @return 记录
     */
    List<TerminalTest> terminalTests(@Param("terminalCode") String terminalCode);
}
