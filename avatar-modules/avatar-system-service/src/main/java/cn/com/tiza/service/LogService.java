package cn.com.tiza.service;

import cn.com.tiza.context.UserInfo;
import cn.com.tiza.dao.LogDao;
import cn.com.tiza.domain.Log;
import cn.com.tiza.service.dto.LogQuery;
import cn.com.tiza.service.mapper.LogMapper;
import cn.com.tiza.web.rest.dto.LogCommand;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 0837
 * 日志服务
 */
@Slf4j
@Service
public class LogService {
    @Autowired
    private LogDao logDao;
    @Autowired
    private LogMapper logMapper;

    public PageQuery<Log> findAll(LogQuery logQuery) {
        PageQuery pageQuery = logQuery.toPageQuery();
        pageQuery.setOrderBy(" log.id desc");
        logDao.getLogInfo(pageQuery);
        return pageQuery;
    }

    /**
     * @param command 前台传回的实体
     * @return Log实体
     */
    public Log create(LogCommand command) {
        Log logInfo = logMapper.commandToLog(command);
        logDao.insert(logInfo);
        log.debug("Created Information for User: {}", logInfo);
        return logInfo;
    }

    public void logLogin(UserInfo user) {
        create(new LogCommand(1, "用户登录", user));
    }

    public void logLogOut(UserInfo user) {
        create(new LogCommand(3, "用户退出", user));
    }
}
