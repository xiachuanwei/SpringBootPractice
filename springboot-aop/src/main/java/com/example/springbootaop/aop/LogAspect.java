package com.example.springbootaop.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 日志切面
 */
@Aspect
@Component
public class LogAspect {
    @Resource
    private HttpServletRequest request;

    @Pointcut("execution(public * com.example.springbootaop.controller.*.*(..))")
    public void log() {
    }

    @Before("log()")
    public void deBefore(JoinPoint joinPoint) {
        System.out.println(" -----------Before advice-----------");
        System.out.println("HTTP_METHOD : " + request.getMethod());
        System.out.println("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        System.out.println("ARGS : " + Arrays.toString(joinPoint.getArgs()));

    }

    @After("log()")
    public void after(JoinPoint joinPoint) {
        System.out.println("-----------After finally advice-----------");
    }

    @AfterReturning(returning = "returning", pointcut = "log()")
    public void doAfterReturning(JoinPoint joinPoint, Object returning) {
        System.out.println("After returning advice returning : " + returning);
    }

    @AfterThrowing(throwing = "throwing", pointcut = "log()")
    public void throwss(JoinPoint joinPoint, Exception throwing) {
        System.out.println("After throwing advice ：throwing " + throwing.getMessage());
    }

    @Around("log()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) {
        System.out.println("-----------Around advice--------");
        try {
            Object returning = proceedingJoinPoint.proceed();
            System.out.println("Around advice returning : " + returning);
            return returning;
        } catch (Throwable throwing) {
            System.out.println("Around advice ：throwing " + throwing.getMessage());
            return null;
        }
    }
}