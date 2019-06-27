package com.boco.nscs.core.annotion;

import java.lang.annotation.*;

/**
 * Created by 操 on 2017/01/14.
 * 允许匿名访问
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Anonymous {
}
