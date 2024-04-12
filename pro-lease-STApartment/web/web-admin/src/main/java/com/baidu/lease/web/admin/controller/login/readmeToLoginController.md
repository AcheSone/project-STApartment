Token认证登陆的技术说明和开发步骤
    什么是JWT？
    Json Web Token（用于身份验证的轻量级传输数据，没错本质上还是JSON）


关于验证必须要做的事情，用户登陆流程

    1.用户发送登陆请求，期间进行包括图形验证码、用户账号密码的验证，若是通过，则进行下一步。
    2.客户端生成Token令牌（包含有效时间以及用户相关信息，分为头部[保存负载和签名的加密方式]、
        负载[保存信息的主体，可自定义标签]、签名[保存有效验证的逻辑，包括限定时间等，保证Token不被篡改]三部分）
        ，颁发令牌并存储在用户端，不占用客户端资源。
    3.后续用户端请求均使用Token进行认证，在有效期内无需再进行登陆认证。

关于登陆要做的事，后端逻辑流程

    1.前端：登陆前请求，请求图形验证码。
    2.后端：接收请求，生成图形验证码(EasyCaptcha)，以UUID生成Key，验证码为value，将图形验证码源数据存入redis缓存区，并设置TTL。
    3.后端：准备就绪后，将UUID和生成的验证码图形响应给前端,(UUID的作用，作为redis缓存数据的key，是获取验证码的依据)。
    4.前端：将用户输入的用户名密码、以及验证码封装，和UUID一同发送给后端。
    5.后端：根据UUID，获得redis的value值，并且进行检验，然后进行用户名密码校验。。
    6.后端：上述操作无问题，则生成JWT，并且将JWT响应给前端。

    重新访问时（重复流程）：
    1.前端：发出请求并携带JWT用作验证。
    2.后端：验证携带的JWT有效性，若是有效合法，则允许请求操作，并将操作得到的数据响应给前端。
    2.后端：携带的JWT若是不合法，则进行重新登陆验证。



要使用的技术：
    
1.EasyCaptcha，根据数据源生成图形验证码，

    <dependency>
        <groupId>com.github.whvcse</groupId>
        <artifactId>easy-captcha</artifactId>
    </dependency>

2.JWT，根据算法生成封装了加密方式、用户数据、有效时间等信息的字符串，解析后得到的是JSON格式的字符信息。
    
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
    </dependency>

    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <scope>runtime</scope>
    </dependency>

    <dependency>
         <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
         <scope>runtime</scope>
    </dependency>

3.Redis，这里使用的是redisTemplate

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>

4.UUID，根据算法生成一个指定长度的字符串，通常用作密码的密文形式。这里用作密码加密和生成图形验证码的Key

5.SpringBoot提供的拦截器
