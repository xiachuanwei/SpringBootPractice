package com.example.springbootasync.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

@Component
@Slf4j
public class AsyncTestBean implements ApplicationRunner {

    @Autowired
    private AsyncTask task;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("-----------执行主线程start-----------");

        task.doVoidTask();
        Future<String> future = task.doFutureTask();
        task.doExceptionTask();
        System.out.println("-----------执行主线程end-----------");
        System.out.println(future.get());

    }
}