package cn.com.tiza.service.jobs;

import cn.com.tiza.config.ApplicationProperties;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.QuartzJobBean;

import static org.quartz.CronScheduleBuilder.cronSchedule;

/**
 * @author tz0920
 */
@Configuration
public class QuartzScheduleConfiguration {
    public static final String VEHICLE_GROUP = "vehicle-group";

    public static final String TERMINAL_GROUP = "terminal-group";
    /**
     * 车辆服务到期
     */
    private static final JobKey VEHICLE_SERVICE_EXPIRE_JOB_KEY = JobKey.jobKey("vehicleServiceExpireJob", VEHICLE_GROUP);
    private static final TriggerKey VEHICLE_SERVICE_EXPIRE_TRIGGER_KEY = TriggerKey.triggerKey("vehicleServiceExpireTrigger", VEHICLE_GROUP);

    /**
     * 控制器锁车
     */
    private static final JobKey CONTROLLER_LOCK_JOB_KEY = JobKey.jobKey("controllerLockJob", TERMINAL_GROUP);
    private static final TriggerKey CONTROLLER_LOCK_TRIGGER_KEY = TriggerKey.triggerKey("controllerLockTrigger", TERMINAL_GROUP);

    /**
     * 调试状态
     */
    private static final JobKey VEHICLE_TEST_STATUS_JOB_KEY = JobKey.jobKey("vehicleTestStatusJob", VEHICLE_GROUP);
    private static final TriggerKey VEHICLE_TEST_STATUS_TRIGGER_KEY = TriggerKey.triggerKey("vehicleTestStatusTrigger", VEHICLE_GROUP);

    /**
     * 保养记录
     */
    private static final JobKey MAINTENANCE_LOG_JOB_KEY = JobKey.jobKey("maintenanceLogJob", VEHICLE_GROUP);
    private static final TriggerKey MAINTENANCE_LOG_TRIGGER_KEY = TriggerKey.triggerKey("maintenanceLogTrigger", VEHICLE_GROUP);

    /**
     * 转发数据压缩（Compress）
     */
    public static final String FWP_GROUP = "forward-group";
    private static final JobKey COMPRESS_JOB_KEY = JobKey.jobKey("forwardJob", FWP_GROUP);
    private static final TriggerKey COMPRESS_TRIGGER_KEY = TriggerKey.triggerKey("forwardTrigger", FWP_GROUP);

    @Autowired
    private Scheduler scheduler;

    @Bean
    public Object startJobs(ApplicationProperties properties) throws SchedulerException {
        //每次启动前检查是否存在-如果不存在就启动Job
        //也可以在配置文件中设置每次启动时覆盖配置

        //转发任务
        if (!scheduler.checkExists(COMPRESS_TRIGGER_KEY)) {
            scheduler.scheduleJob(newJobDetail(ForwardDataCompressJob.class, COMPRESS_JOB_KEY),
                    newCronTrigger(COMPRESS_TRIGGER_KEY, properties.getQuartzJob().getForwardDataCompressCron()));
        } else {
            updateCron(COMPRESS_TRIGGER_KEY, properties.getQuartzJob().getForwardDataCompressCron());
        }

        //spring.quartz.overwrite-existing-jobs
        //服务到期
        if (!scheduler.checkExists(VEHICLE_SERVICE_EXPIRE_TRIGGER_KEY)) {
            scheduler.scheduleJob(newJobDetail(VehicleServiceExpireJob.class, VEHICLE_SERVICE_EXPIRE_JOB_KEY),
                    newCronTrigger(VEHICLE_SERVICE_EXPIRE_TRIGGER_KEY, properties.getQuartzJob().getVehicleServiceExpireCron()));
        } else {
            updateCron(VEHICLE_SERVICE_EXPIRE_TRIGGER_KEY, properties.getQuartzJob().getVehicleServiceExpireCron());
        }
        //获取控制器锁车最终结果
        if (!scheduler.checkExists(CONTROLLER_LOCK_TRIGGER_KEY)) {
            scheduler.scheduleJob(newJobDetail(ControllerLockJob.class, CONTROLLER_LOCK_JOB_KEY),
                    newCronTrigger(CONTROLLER_LOCK_TRIGGER_KEY, properties.getQuartzJob().getControllerLockCron()));
        } else {
            updateCron(CONTROLLER_LOCK_TRIGGER_KEY, properties.getQuartzJob().getControllerLockCron());
        }
        //调试状态定时遍历修改
        if (!scheduler.checkExists(VEHICLE_TEST_STATUS_TRIGGER_KEY)) {
            scheduler.scheduleJob(newJobDetail(VehicleTestServiceJob.class, VEHICLE_TEST_STATUS_JOB_KEY),
                    newCronTrigger(VEHICLE_TEST_STATUS_TRIGGER_KEY, properties.getQuartzJob().getVehicleServiceExpireCron()));
        } else {
            updateCron(VEHICLE_TEST_STATUS_TRIGGER_KEY, properties.getQuartzJob().getVehicleServiceExpireCron());
        }
        //保养记录定时生成
        if (!scheduler.checkExists(MAINTENANCE_LOG_TRIGGER_KEY)) {
            scheduler.scheduleJob(newJobDetail(MaintenanceLogJob.class, MAINTENANCE_LOG_JOB_KEY),
                    newCronTrigger(MAINTENANCE_LOG_TRIGGER_KEY, properties.getQuartzJob().getMaintenanceLogCron()));
        } else {
            updateCron(MAINTENANCE_LOG_TRIGGER_KEY, properties.getQuartzJob().getMaintenanceLogCron());
        }
        return new Object();
    }

    private void updateCron(TriggerKey key, String cronToUpdate) throws SchedulerException {
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(key);
        if (Trigger.TriggerState.ERROR.equals(scheduler.getTriggerState(key))
                || !cronToUpdate.equalsIgnoreCase(trigger.getCronExpression())) {
            scheduler.rescheduleJob(key, newCronTrigger(key, cronToUpdate));
        }
    }


    /**
     * 任务
     */
    private JobDetail newJobDetail(Class<? extends QuartzJobBean> cls, JobKey jobKey) {
        return JobBuilder.newJob(cls)
                .withIdentity(jobKey)
                .build();
    }

    /**
     * 定时触发器
     */
    private Trigger newCronTrigger(TriggerKey key, String cron) {
        // 定义一个Trigger
        return TriggerBuilder.newTrigger()
                .withIdentity(key)
                .startNow()
                .withSchedule(cronSchedule(cron))
                .build();
    }
}
