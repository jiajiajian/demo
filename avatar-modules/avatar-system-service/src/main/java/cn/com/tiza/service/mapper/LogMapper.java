package cn.com.tiza.service.mapper;

import cn.com.tiza.dao.UserDao;
import cn.com.tiza.domain.Log;
import cn.com.tiza.web.rest.dto.LogCommand;
import cn.com.tiza.web.rest.vm.LogVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Log实体和DTO转换服务,不使用BeanUtils.copyProperties
 * @author tiza
 */
@Component
public class LogMapper {
    @Autowired
    private UserDao userDao;

    public Log commandToLog(LogCommand command) {
        if (command == null) {
            return null;
        }
        Log log = new Log();
        log.setLogType(command.getLogType());
        log.setOperateTime(command.getOperateTime());
        log.setOperateAccount(command.getOperateAccount());
        log.setOperateRealName(command.getOperateRealName());
        log.setFunctionId(command.getFunctionId());
        log.setFunctionName(command.getFunctionName());
        log.setOperateContent(command.getOperateContent());
        log.setIpAddress(command.getIpAddress());
        log.setIpCity(command.getIpCity());
        log.setHostName(command.getHostName());
        log.setBrowserName(command.getBrowserName());
        if (null != command.getOrganizationId()) {
            log.setOrganizationId(command.getOrganizationId());
        } else {
            log.setOrganizationId(userDao.getOrgIdByLoginName(command.getOperateAccount()));
        }
        return log;
    }

    public LogVM logToVM(Log log) {
        if (log == null) {
            return null;
        }
        LogVM vm = new LogVM();
        vm.setLogType(log.getLogType());
        vm.setOperateTime(log.getOperateTime());
        vm.setOperateAccount(log.getOperateAccount());
        vm.setOperateContent(log.getOperateContent());
        vm.setFunctionName(log.getFunctionName());
        vm.setIpAddress(log.getIpAddress());
        return vm;
    }

    public List<LogVM> logListToVMList(List<Log> logs) {
        return logs.stream()
                .map(this::logToVM)
                .collect(Collectors.toList());
    }
}
