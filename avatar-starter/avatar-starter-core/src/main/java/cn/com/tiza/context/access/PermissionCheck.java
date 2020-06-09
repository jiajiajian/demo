package cn.com.tiza.context.access;

import java.lang.annotation.*;

/**
 * 权限点验证
 * @author tiza
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface PermissionCheck {

	int[] value();

	String method() default "POST";

	String script() default "";

	String description() default "";
}
