package com.baidu.lease.web.admin.service.impl;

import com.baidu.lease.web.admin.service.FileService;
import io.minio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.baidu.lease.common.minio.MinioProperties;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    //这里导入的MinionProperties对象是common对象
    @Autowired
    private MinioProperties properties;

    @Autowired
    private MinioClient client;

    @Override
    public String upload(MultipartFile file) {

        try {
            boolean bucketExists = client.bucketExists(BucketExistsArgs.builder().bucket(properties.getBucketName()).build());
            if (!bucketExists) {
                client.makeBucket(MakeBucketArgs.builder().bucket(properties.getBucketName()).build());
                client.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(properties.getBucketName()).config(createBucketPolicyConfig(properties.getBucketName())).build());
            }

            String filename = new SimpleDateFormat("yyyyMMdd").format(new Date()) + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
            client.putObject(PutObjectArgs.builder().
                    bucket(properties.getBucketName()).
                    object(filename).
                    stream(file.getInputStream(), file.getSize(), -1).
                    contentType(file.getContentType()).build());

            return String.join("/", properties.getEndpoint(), properties.getBucketName(), filename);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String createBucketPolicyConfig(String bucketName) {

        return """
                {
                  "Statement" : [ {
                    "Action" : "s3:GetObject",
                    "Effect" : "Allow",
                    "Principal" : "*",
                    "Resource" : "arn:aws:s3:::apartmentjpg/*"
                  } ],
                  "Version" : "2012-10-17"
                }
                """.formatted(bucketName);
    }

}
