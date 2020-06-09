package cn.com.tiza.service.jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author tz0920
 */
@Slf4j
@Component
@Transactional(rollbackFor = Exception.class)
public class ForwardDataCompressJob extends QuartzJobBean {
    @Autowired
    private ForwardDataCompressClient forwardDataCompressClient;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("run forwardDataCompress job!");
        forwardDataCompressClient.forwardDataCompressJob();
    }
}
