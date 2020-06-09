package cn.com.tiza.service.jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * @author tz
 */
@Slf4j
@Component
public class VehicleTestServiceJob extends QuartzJobBean {
    @Autowired
    private VehicleClient vehicleClient;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("------------vehicleTestServiceJob run--------------");
        vehicleClient.vehicleTestServiceJob();
    }

}
