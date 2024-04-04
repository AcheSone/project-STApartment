package com.baidu;

import io.minio.*;
import io.minio.errors.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class App {
    public static void main(String[] args) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        //创建minion桶连接对象
        MinioClient minioClient = MinioClient.builder()
                .credentials("minioadmin","minioadmin")
                .endpoint("http://192.168.179.128:9000").build();

        //判断指定的桶是否存在
        boolean flat = minioClient.bucketExists(BucketExistsArgs.builder().bucket("hello-minio").build());

        //桶若是不存在则进行创建并指定桶权限
        if (!flat){
            minioClient.makeBucket(MakeBucketArgs.builder().bucket("hello-minio").build());
            String policy = """
                        {
                          "Statement" : [ {
                            "Action" : "s3:GetObject",
                            "Effect" : "Allow",
                            "Principal" : "*",
                            "Resource" : "arn:aws:s3:::hello-minio/*"
                          } ],
                          "Version" : "2012-10-17"
                        }""";
            //设置指定桶的权限规则
            minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket("hello-minio").config(policy).build());
            minioClient.uploadObject(UploadObjectArgs.builder().bucket("hello-minio").object("牢大.jpg").filename("C:\\Users\\fuzihao\\Desktop\\牢大.jpg").build());
            System.out.println("创建桶并上传成功");
        }


    }
}
