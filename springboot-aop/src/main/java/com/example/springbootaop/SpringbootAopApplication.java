package com.example.springbootaop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan("com.example.springbootaop.filter")
public class SpringbootAopApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootAopApplication.class, args);
    }

}
