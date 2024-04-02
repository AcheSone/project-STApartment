package com.baidu;

import com.baidu.crud.pojo.User;
import com.baidu.crud.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestDeletedAndPaging {

    @Autowired
    private UserService userService;

    @Test
    public void test1(){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        /*User userServiceById = userService.getById(1);*/
        lambdaQueryWrapper.eq(User::getId,3);
        System.out.println(userService.remove(lambdaQueryWrapper));
    }

    @Test
    public void test2(){
        Page<User> page = Page.of(2,2);
        userService.list(page).forEach(System.out::println);
    }

}
