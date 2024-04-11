引入依赖
<dependency>
<groupId>io.minio</groupId>
<artifactId>minio</artifactId>
<version>8.5.3</version>
</dependency>

使用minion技术可以使我们将图片等静态资源文件，作为一个对象进行存储
概念：
    
    桶：用作存储资源对象的容器
    端点：http://IP地址:9000     其中9000是访问资源对象的端点，9001是后台管理系统的端口号


在使用minion前，要先创建桶连接对象MinionClient，一切对minion的操作都是通过连接对象进行的
为了方便使用桶连接对象，通常将其纳入IOC容器的管理。
    
    以下是创建桶连接对象配置访问的IP地址并且将用户名密码设置给连接对象的操作
    MinioClient build = MinioClient.builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();