package com.boco.nscs.core.annotion;

import java.lang.annotation.*;

@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    String name() default "";
    int limit() default 10;
}
