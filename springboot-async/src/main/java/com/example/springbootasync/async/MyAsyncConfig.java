package com.example.springbootasync.async;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class MyAsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // 核心线程数
        executor.setMaxPoolSize(30); // 最大线程数
        executor.setQueueCapacity(1); // 线程池缓冲队列
        executor.setWaitForTasksToCompleteOnShutdown(true); // 关闭线程池时等待所有任务完成
        executor.setAwaitTerminationSeconds(60 * 10); // 关闭线程池等待时间默认为0，等待xx秒后停止
        executor.setThreadNamePrefix("MyAsync-"); // 线程名称前缀
        executor.initialize(); // 初始化
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new MyAsyncExceptionHandler();
    }
}