package com.gmail.helloknife4j.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

//此类是Knife4j要纳入IOC管理的Knife4j组件的组件容器类

public class Knife4jConfiguration {

    @Bean
    public OpenAPI openAPI() {
        Contact contact = new Contact();
        contact.setName("韦小宝");
        contact.setEmail("weixiaobao@127.com");
        return new OpenAPI()
                .info(new Info()
                        .title("hello-knife4j项目API")
                        .contact(contact)
                        .version("1.0")
                        .description("hello-knife4j项目的接口文档"));
    }

    @Bean
    public GroupedOpenApi userAPI() {
        return GroupedOpenApi.builder().group("用户信息管理").
                //设置的是要生成API文档的Controller所在网页路径，也就是该控制器设置的Mapping属性
                pathsToMatch("/knife4j/**").
                build();
    }

    @Bean
    public GroupedOpenApi systemAPI() {
        return GroupedOpenApi.builder().group("产品信息管理").
                pathsToMatch("/product/**").
                build();
    }
    @Bean
    public GroupedOpenApi testAPI(){
        return  GroupedOpenApi.builder().group("测试用户信息管理").pathsToMatch("/knife4j/**").build();
    }
}
