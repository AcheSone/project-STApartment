package com.gmail.helloknife4j.controller;

import com.gmail.helloknife4j.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/knife4j")

//该注解用作对该类（或接口）进行描述，并且在API文档中，进行分类，相同Tag的会被放在同一个菜单
@Tag(name = "HelloKnife4jController",description = "用户信息管理")
public class HelloKnife4jController {

    //该注解用作对接口中的方法作用进行描述
    @Operation(summary = "根据id获取用户信息")
    @GetMapping("/test")
    public User getUserById(@Parameter(description = "用户id",name = "id") @RequestParam Long id){
        //上面形参列表中的@Parameter注解是用作描述方法形参对应的信息
        User user = new User(1l,"tom",34,"23245455@128.com");
        return user;
    }
}
