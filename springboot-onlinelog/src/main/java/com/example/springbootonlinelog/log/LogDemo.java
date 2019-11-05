package com.example.springbootonlinelog.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class LogDemo implements ApplicationRunner {

    protected final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public void run(ApplicationArguments args) throws Exception {
        while (true) {
            Thread.sleep(1000);
            logger.debug("输出 DEBUG 信息");
            logger.info("输出 INFO 信息");
            logger.error("输出 ERROR 信息");
        }
    }
}
