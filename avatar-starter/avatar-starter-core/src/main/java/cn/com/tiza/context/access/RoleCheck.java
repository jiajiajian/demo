package cn.com.tiza.context.access;

import java.lang.annotation.*;

/**
 * 角色检查
 * @author tiza
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RoleCheck {

    /**
     * 角色
     * @return
     */
    String[] value();

    /**
     * 权限描述
     * @return
     */
    String description() default "";
}
