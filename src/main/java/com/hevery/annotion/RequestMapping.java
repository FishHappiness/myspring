package com.hevery.annotion;

import java.lang.annotation.*;

/**
 * @description: 定义RequestMapping注解，用于分发请求
 * @author: hongyu.zhang
 * @create: 2018-03-19
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
    String value() default "";
}
