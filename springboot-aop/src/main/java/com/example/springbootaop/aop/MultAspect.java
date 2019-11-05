package com.example.springbootaop.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MultAspect {

    @Pointcut("execution(public * com.example.springbootaop.controller.*.*(..))")
    public void point1() {
    }

    @Pointcut("execution(@com.example.springbootaop.aop.UserLog * *(..))")
    public void point2() {
    }

    @Pointcut("@annotation(com.example.springbootaop.aop.UserLog)")
    public void point3() {
    }

    @Pointcut("@target(com.example.springbootaop.aop.UserLog)")
    public void point4() {
    }

}
