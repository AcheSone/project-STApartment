package com.baidu.lease.common.minio;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Configuration
@EnableConfigurationProperties(MinioProperties.class)
//通过此注解中的import注解，可以将MinionProperties文件纳入IOC容器管理
public class MinioConfiguration {

    @Autowired
    private MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(minioProperties.getAccessKey(),minioProperties.getSecretKey()).build();

        //在创建桶之前通过桶名称判断是否存在该桶，若是存在则跳过创建，若是不存在，则创建并设定权限
        boolean flat = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucketName()).build());
        if (!flat){
            //桶不存在走这个逻辑，进行桶创建
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucketName()).build());

            //书写桶权限规则的JSON文件配置，以桶名为单位，其中百分号s是占位符，用于动态替代桶名称
            String policy = """
                    {
                                  "Statement" : [ {
                                    "Action" : "s3:GetObject",
                                    "Effect" : "Allow",
                                    "Principal" : "*",
                                    "Resource" : "arn:aws:s3:::%s/*"
                                  } ],
                                  "Version" : "2012-10-17"
                                }
                    """.formatted(minioProperties.getBucketName());
            //设置桶规则，将上述设置set进规则指定中
            minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(minioProperties.getBucketName()).config(policy).build());
        }
        return minioClient;
    }

}
