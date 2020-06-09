package cn.com.tiza.annotation;

import cn.com.tiza.dto.AlarmType;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AlarmConsumeStrategyAnnotation {
    AlarmType alarmType();
}
