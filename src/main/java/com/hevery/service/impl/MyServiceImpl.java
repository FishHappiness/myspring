package com.hevery.service.impl;

import com.hevery.annotion.Service;
import com.hevery.service.MyService;

/**
 * @description: ${description}
 * @author: hongyu.zhang
 * @create: 2018-03-20
 **/
@Service("myServiceImpl")
public class MyServiceImpl implements MyService {
    public String getUser(String name) {
        return "hello";
    }
}
