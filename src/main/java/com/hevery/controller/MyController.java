package com.hevery.controller;

import com.hevery.annotion.Controller;
import com.hevery.annotion.Qualifier;
import com.hevery.annotion.RequestMapping;
import com.hevery.service.MyService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description: ${description}
 * @author: hongyu.zhang
 * @create: 2018-03-19
 **/
@Controller("hello")
public class MyController {
    @Qualifier("myServiceImpl")
    MyService myService;


    @RequestMapping("get")
    public String getUser(HttpServletRequest req, HttpServletResponse rep,String name){
        myService.getUser(name);
        return "success";
    }
}
