package com.example.springbootexception.controller;

import com.example.springbootexception.conf.ExceptionEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    private Map<String, Object> map = new HashMap<>();

    @GetMapping("/getUser")
    public Object getUser() {
        Object user = map.get("Tom");
        ExceptionEnum.USER_NOT_FOUND.notNull(user, "");
        return "test1";
    }

    @GetMapping("/getUserArgs")
    public Object getUserArgs(@RequestParam String userName) {
        Object user = map.get(userName);
        ExceptionEnum.USER_NOT_FOUND.notNull(user, userName);
        return "test1";
    }

    @GetMapping("/test")
    public Object test() {
        return 1 / 0;
    }
}
