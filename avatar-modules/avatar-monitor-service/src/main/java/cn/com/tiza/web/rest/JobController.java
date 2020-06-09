package cn.com.tiza.web.rest;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author tz0920
 */
@Slf4j
@RestController
@RequestMapping("/job")
public class JobController {
    @Autowired
    private Scheduler scheduler;


    @DeleteMapping("/{name}/{group}")
    public void deleteJob(@PathVariable String name, @PathVariable String group) throws Exception {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (Objects.isNull(jobDetail)) {
            return;
        }
        scheduler.deleteJob(jobKey);
    }
}
