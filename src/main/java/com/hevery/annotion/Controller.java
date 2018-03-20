package com.hevery.annotion;

import java.lang.annotation.*;

/**
 * @description: 定义Controller注解，用于标注Controller
 * @author: hongyu.zhang
 * @create: 2018-03-19
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {
    String value() default "";
}
