package com.baidu.lease.common.minio;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MinioProperties.class)
//开启属性配置类的使用
public class MinioConfiguration {


    @Autowired
    private MinioProperties minioProperties;


    //创建minio的连接对象，并通过自动装配application.yaml中的属性，为minion的连接对象赋值
    @Bean
    public MinioClient minioClient(){
        MinioClient build = MinioClient.builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();

        return build;
    }

}
