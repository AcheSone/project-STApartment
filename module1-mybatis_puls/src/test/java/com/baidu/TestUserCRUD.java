package com.baidu;

import com.baidu.crud.mapper.UserMapper;
import com.baidu.crud.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestUserCRUD {
    @Autowired
    private UserMapper userMapper;


    @Test
    public void test1(){
        userMapper.selectList(null).forEach(System.out::println);

        //通过设置对象的属性值，再将对象作为修改依据传入updateById方法，如此实现修改
        User user1 = userMapper.selectById(4l);
        user1.setName("张三");
        userMapper.updateById(user1);
    }

    @Test
    public void test2(){
        User user = new User(null,"陈思诚",55,"777777@163.com",1);
        userMapper.insert(user);
        System.out.println(userMapper.selectById(user.getId()));
    }

    @Test
    public void test3(){
        userMapper.selectList(null).forEach(System.out::println);
        
    }

}
