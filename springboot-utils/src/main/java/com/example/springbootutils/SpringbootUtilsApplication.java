package com.example.springbootutils;

import com.example.springbootutils.utils.dbpool.DBInfo;
import com.example.springbootutils.utils.dbpool.DatabaseConnTool;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
public class SpringbootUtilsApplication {

    public static void main(String[] args) {
//        SpringApplication.run(SpringbootUtilsApplication.class, args);
        DBInfo dbInfo = new DBInfo("com.mysql.cj.jdbc.Driver", "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true", "root", "root");
        System.out.println(DatabaseConnTool.querySql("select * from student", dbInfo).size());
    }

}
