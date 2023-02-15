package org.boson.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {

    /**
     * @return 操作类型
     */
    String value() default "";

}
