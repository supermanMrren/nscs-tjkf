package com.boco.nscs.core.annotion;

import java.lang.annotation.*;

/**
 * Created by CC on 2017/1/22.
 * 日志记录
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BusiLog {
    String action() default "";
    String logInfo() default "";
    String key() default "id";
    String ModuleId() default "";
}
