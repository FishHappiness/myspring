package com.hevery.annotion;

import java.lang.annotation.*;

/**
 * @description: 定义Service注解，用于标注服务
 * @author: hongyu.zhang
 * @create: 2018-03-19
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {
    String value() default "";
}
