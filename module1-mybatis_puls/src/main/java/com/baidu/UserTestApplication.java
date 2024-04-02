package com.baidu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.baidu.crud.mapper")
public class UserTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserTestApplication.class);
    }
}
