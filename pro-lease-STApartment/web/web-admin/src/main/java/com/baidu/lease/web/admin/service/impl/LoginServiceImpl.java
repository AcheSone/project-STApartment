package com.baidu.lease.web.admin.service.impl;

import com.baidu.lease.common.constant.RedisConstant;
import com.baidu.lease.web.admin.service.LoginService;
import com.baidu.lease.web.admin.vo.login.CaptchaVo;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    //此方法用作获取图片验证码的图片信息和验证码的Key
    public CaptchaVo getCaptcha() {
        //生成一个specCaptcha对象并设置验证码图像的大小
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 4);
        //为图形对象设置一个默认的图像格式，默认为PNG格式
        specCaptcha.setCharType(Captcha.TYPE_DEFAULT);

        //生成要存入redis缓存区的图片对象key和value值，key使用UUID生成，防止冲突，生成value之后进行算法加密，得到图片的加密信息
        String code = specCaptcha.text().toLowerCase();
        //通过自定义前缀+UUID生成的方式保证key唯一
        String key = RedisConstant.ADMIN_LOGIN_PREFIX + UUID.randomUUID();
        //当设置完成之后，就可以对图形对象进行加密编码生成
        String image = specCaptcha.toBase64();


        //将key和code存入redis当中，并且返回CaptchaVo对象（封装了image和key的对象）给前端，供前端获取图形验证码
        //同时设置对象存活时间为60秒，存活策略为seconds
        redisTemplate.opsForValue().set(key,code,RedisConstant.ADMIN_LOGIN_CAPTCHA_TTL_SEC, TimeUnit.SECONDS);
        return new CaptchaVo(image,key);
    }
}
