package com.baidu;

import com.baidu.crud.mapper.UserMapper;
import com.baidu.crud.pojo.User;
import com.baidu.crud.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestUserAutoService {
    @Autowired
    private UserServiceImpl userService;

    @Test
    public void test1(){
        //查询操作
        userService.list().forEach(System.out::println);

        //通过判断对象中的主键值是否为空，是则执行插入操作，否则执行修改操作
        userService.saveOrUpdate(new User(null,"王浩栋",33,"haodong11@qq.com"));
    }

    @Test
    public void test2(){
        UserMapper baseMapper = userService.getBaseMapper();
        baseMapper.selectList(null).forEach(System.out::println);
    }

    @Test
    public void test3(){
        System.out.println(userService.removeById(6));
    }

}
