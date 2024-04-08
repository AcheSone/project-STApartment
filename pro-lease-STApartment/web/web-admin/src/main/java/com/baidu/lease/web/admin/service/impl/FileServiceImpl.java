package com.baidu.lease.web.admin.service.impl;

import com.baidu.lease.web.admin.service.FileService;
import io.minio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.baidu.lease.common.minio.MinioProperties;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

//此类用作通过minion上传文件，并且返回对应的文件名称供后端存储查询，供前端进行访问
@Service
public class FileServiceImpl implements FileService {

    //这里导入的MinionProperties对象是common对象
    @Autowired
    private MinioProperties properties;

    @Autowired
    private MinioClient minioClient;

    @Override
    public String upload(MultipartFile file) throws Exception {
        //通过调用MultipartFile中的方法获取原始的文件名称
        String originalFilename = file.getOriginalFilename();

        //创建新的文件名称，（拼接了UUID和日期，还有原本的文件名称）,作用是防止文件重名
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        //获取上面的日期格式对象，创建日期对象并且设置日期格式，同时拼接上UUID，
        // UUID要做处理，将特殊的字符转换为空字符串，最后拼接上原文件名
        originalFilename = sdf.format(new Date())+"/"+ (UUID.randomUUID().toString().replaceAll("-",""))+ "_" + originalFilename;

        minioClient.putObject(PutObjectArgs.builder()
                .bucket(properties.getBucketName())
                //获取上传文件的流，然后通过minion提供的object方法对文件名称进行设置
                .stream(file.getInputStream(),file.getSize(),-1)
                .object(originalFilename)
                .contentType(file.getContentType()).build());

        String format = String.format("%s/%s", properties.getEndpoint(), properties.getBucketName() + "/" + originalFilename);

        System.out.println(format);
        //最后将获取到的文件名称返回给后端，供其做存入数据库操作。
        return format;
    }

















    //以下是课件的做法
    /*@Override
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
    }*/

}
