package com.baidu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.baidu.crud.mapper")
@EnableTransactionManagement
public class UserTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserTestApplication.class);
    }
}
