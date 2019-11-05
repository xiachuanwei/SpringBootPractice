package com.example.springbootexception.conf;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionEnum implements Assert {


    USER_NOT_FOUND(1001, "用户{0}不存在"),
    ;

    // 异常码
    private int code;
    // 异常信息
    private String message;
}
