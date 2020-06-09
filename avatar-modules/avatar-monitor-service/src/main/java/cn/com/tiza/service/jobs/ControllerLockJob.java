package cn.com.tiza.service.jobs;

import cn.com.tiza.web.rest.ControllerLockClient;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * 获取控制器锁车最终结果
 *
 * @author villas
 */
@Slf4j
@Component
public class ControllerLockJob extends QuartzJobBean {

    @Autowired
    ControllerLockClient lockClient;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("------------获取控制器锁车最终结果 ControllerLockJob run--------------");
        lockClient.scheduleControllerLock();
    }
}
