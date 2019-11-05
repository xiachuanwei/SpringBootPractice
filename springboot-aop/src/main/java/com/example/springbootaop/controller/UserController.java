package com.example.springbootaop.controller;

import com.example.springbootaop.aop.UserLog;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @RequestMapping("/test1")
    public Object test1() {
        return "test1";
    }

    @RequestMapping("/test2")
    public Object test2() {
        return 1 / 0;
    }

    @RequestMapping("/test3")
    @UserLog(desc = "test3")
    public Object test3() {
        return "test3";
    }
}
