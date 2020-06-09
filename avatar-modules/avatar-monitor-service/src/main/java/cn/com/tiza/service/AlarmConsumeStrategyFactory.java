package cn.com.tiza.service;

import cn.com.tiza.annotation.AlarmConsumeStrategyAnnotation;
import cn.com.tiza.dto.AlarmType;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 报警消费策略工厂
 *
 * @author tz0920
 */
@Slf4j
@Component
public class AlarmConsumeStrategyFactory implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    private static ConcurrentHashMap<AlarmType, AlarmService> strategyFactory = new ConcurrentHashMap<>();

    /**
     * 根据报警类型获取处理策略
     *
     * @param alarmType 报警类型
     * @return
     */
    public static AlarmService getByAlarmType(AlarmType alarmType) {
        return strategyFactory.get(alarmType);
    }

    /**
     * 自动注册
     */
    private void autoRegister() {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(AlarmService.class.getPackage().getName()))
                .filterInputsBy(fileName -> fileName.endsWith(".class"))
                .setScanners(new SubTypesScanner()));
        Set<Class<? extends AlarmService>> alarmStrategyClassSet = reflections.getSubTypesOf(AlarmService.class);
        if (Objects.nonNull(alarmStrategyClassSet)) {
            for (Class<? extends AlarmService> clazz : alarmStrategyClassSet) {
                //找到报警类型注解，自动完成策略注册
                if (clazz.isAnnotationPresent(AlarmConsumeStrategyAnnotation.class)) {
                    AlarmConsumeStrategyAnnotation annotation = clazz.getAnnotation(AlarmConsumeStrategyAnnotation.class);
                    AlarmType alarmType = annotation.alarmType();
                    strategyFactory.put(alarmType, applicationContext.getBean(clazz));
                }
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        autoRegister();
    }
}
