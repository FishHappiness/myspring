package com.hevery.annotion;

import java.lang.annotation.*;

/**
 * @description: 注入bean
 * @author: hongyu.zhang
 * @create: 2018-03-19
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Qualifier {
    String value() default "";
}
