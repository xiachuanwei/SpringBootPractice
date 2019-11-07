package com.example.springbootasync.async;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

@Component
@Slf4j
public class AsyncTask {
    @Async
    public void doVoidTask() {
        log.info("void task start:" + Thread.currentThread().getName());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("void task end:" + Thread.currentThread().getName());
    }

    @Async
    public Future<String> doFutureTask() {
        log.info("future task start:" + Thread.currentThread().getName());
        Future<String> future;
        try {
            Thread.sleep(2000);
            future = new AsyncResult<>("future result");
        } catch (InterruptedException e) {
            future = new AsyncResult<>("future error");
        }
        log.info("future task end:" + Thread.currentThread().getName());
        return future;
    }

    @Async
    public void doExceptionTask() {
        log.info("exception task start:" + Thread.currentThread().getName());
        try {
            Thread.sleep(1000);
            System.out.println(1 / 0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("exception task end:" + Thread.currentThread().getName());
    }

}
