package com.example.springbootaop.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/*", filterName = "myFilter")
public class MyFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest res, ServletResponse req, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) res;
        HttpServletResponse response = (HttpServletResponse) req;
        System.out.println("过滤器：过滤到URL请求：" + request.getRequestURI());
        long start = System.currentTimeMillis();
        filterChain.doFilter(res, req);
        System.out.println("过滤器：处理完URL结果：" + response.getStatus());
    }

    @Override
    public void destroy() {

    }
}