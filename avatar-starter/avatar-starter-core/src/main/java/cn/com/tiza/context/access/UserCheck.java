package cn.com.tiza.context.access;

import java.lang.annotation.*;

/**
 * 用户检查
 * @author tiza
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface UserCheck {

    /**
     * 用户
     * @return
     */
    String[] value();

    /**
     * 权限描述
     * @return
     */
    String description() default "";
}
