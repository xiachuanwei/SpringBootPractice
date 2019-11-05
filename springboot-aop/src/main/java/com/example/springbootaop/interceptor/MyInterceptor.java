package com.example.springbootaop.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class MyInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
        request.setAttribute("t1", System.currentTimeMillis());
        System.out.println("拦截器:拦截到URL请求：" + request.getRequestURI());
        if (request.getRequestURI().contains("test3")) {
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView mav)
            throws Exception {
        long t1 = (Long) request.getAttribute("t1");
        long t2 = System.currentTimeMillis();
        System.out.println("拦截器:请求接口 " + request.getRequestURI() + " 处理时间：" + (t2 - t1) + "ms");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj, Exception exc)
            throws Exception {
        System.out.println("拦截器:请求结束，清理资源Over");
    }
}
