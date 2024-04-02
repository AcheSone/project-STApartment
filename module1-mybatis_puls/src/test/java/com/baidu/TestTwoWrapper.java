package com.baidu;

import com.baidu.crud.pojo.User;
import com.baidu.crud.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestTwoWrapper {
    @Autowired
    private UserService userService;

    @Test
    public void test1() {
        //通过条件构造器，为查询语句提供where id = 1这个子句的操作
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        QueryWrapper<User> wrapper = queryWrapper.eq("id", 1);
        System.out.println(userService.getOne(wrapper));

    }

    @Test
    public void test2() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //查询小于id小于2且id大于4的用户信息
        queryWrapper.lt("id", 2).or().gt("id", 4);
        userService.list(queryWrapper).forEach(System.out::println);

        //常用方法还有，eq()用于做条件等值
        //orderByDesc()用作排序，降序
        //like()用作模糊查询
        //between()用作查询某个区间
        //and()和or()对应了and和or连接符
        //lt()代表小于，gt()代表大于
    }

    @Test
    public void test3(){
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        //设置表为user，且更改email属性值，并指定id为7的用户更改
        userUpdateWrapper.eq("id",7);
        userUpdateWrapper.set("email","88888866@163.com");
    }

    @Test
    public void test4(){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //通过方法引用来实现拼接where子句，这种方式不会写死某个值，比较方便灵活
        lambdaQueryWrapper.eq(User::getName,"王浩栋");
        System.out.println(userService.getOne(lambdaQueryWrapper));
    }
}
