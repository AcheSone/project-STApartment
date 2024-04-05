package com.baidu.lease.web.admin.custom2.config;

import com.baidu.lease.web.admin.custom2.converter.StringToItemTypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    //得到了比较器当然要注册！所以要实现WebMvcConfigurer进行组件注册，
    // 这个东西我们之前在注册拦截器那时候也使用过

    @Autowired
    private StringToItemTypeConverter stringToItemTypeConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(this.stringToItemTypeConverter);
    }
}
