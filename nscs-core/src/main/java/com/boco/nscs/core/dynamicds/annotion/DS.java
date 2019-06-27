package com.boco.nscs.core.dynamicds.annotion;

import java.lang.annotation.*;

/**
 * 
 * 多数据源标识
 *
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface DS {

	String name() default "";
}
