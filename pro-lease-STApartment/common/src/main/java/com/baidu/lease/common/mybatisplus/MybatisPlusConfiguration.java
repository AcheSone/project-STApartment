package com.baidu.lease.common.mybatisplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//组件扫描也放由common包中的启动类进行
@MapperScan("com.baidu.lease.web.*.mapper")
public class MybatisPlusConfiguration {
    public static void main(String[] args) {
        SpringApplication.run(MybatisPlusConfiguration.class);
    }
}
