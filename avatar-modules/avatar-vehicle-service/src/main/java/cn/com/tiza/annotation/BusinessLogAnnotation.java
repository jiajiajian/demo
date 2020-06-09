package cn.com.tiza.annotation;

import cn.com.tiza.service.dto.BusinessLogType;

import java.lang.annotation.*;

/**
 * @author tz0920
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BusinessLogAnnotation {
    BusinessLogType value();
}
