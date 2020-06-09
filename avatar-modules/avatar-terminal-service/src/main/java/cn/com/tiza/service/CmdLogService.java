package cn.com.tiza.service;


import cn.com.tiza.cmd.command.dao.CommandDao;
import cn.com.tiza.cmd.command.domain.Command;
import cn.com.tiza.service.dto.CmdLogQuery;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service
 * gen by beetlsql 2020-04-16
 *
 * @author tiza
 */
@Slf4j
@Service
public class CmdLogService {

    @Autowired
    private CommandDao commandDao;

    public PageQuery<Command> findAll(CmdLogQuery query) {
        PageQuery<Command> pageQuery = query.toPageQuery();
        commandDao.pageQuery(pageQuery);
        return pageQuery;
    }
}
