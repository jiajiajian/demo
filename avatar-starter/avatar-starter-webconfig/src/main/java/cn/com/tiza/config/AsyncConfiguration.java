package cn.com.tiza.config;

import cn.com.tiza.async.ExceptionHandlingAsyncTaskExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author tiza
 */
@Slf4j
@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfiguration implements AsyncConfigurer, SchedulingConfigurer {

    private final AvatarProperties avatarProperties;

    public AsyncConfiguration(AvatarProperties avatarProperties) {
        this.avatarProperties = avatarProperties;
    }

    @Override
    @Bean(name = "taskExecutor")
    public Executor getAsyncExecutor() {
        log.debug("Creating Async Task Executor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(avatarProperties.getAsync().getCorePoolSize());
        executor.setMaxPoolSize(avatarProperties.getAsync().getMaxPoolSize());
        executor.setQueueCapacity(avatarProperties.getAsync().getQueueCapacity());
        executor.setThreadNamePrefix("avatar-Executor-");
        return new ExceptionHandlingAsyncTaskExecutor(executor);
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(scheduledTaskExecutor());
    }

    @Bean
    public Executor scheduledTaskExecutor() {
        return Executors.newScheduledThreadPool(avatarProperties.getAsync().getCorePoolSize());
    }
}
