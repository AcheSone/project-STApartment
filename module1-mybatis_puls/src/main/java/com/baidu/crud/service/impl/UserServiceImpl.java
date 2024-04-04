package com.baidu.crud.service.impl;

import com.baidu.crud.mapper.UserMapper;
import com.baidu.crud.pojo.User;
import com.baidu.crud.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    //必须指定实体类的Mapper接口，因为ServiceImpl底层调用的BaseMapper实现的增删改查方法
    //必须指定类型为实体类，相当于原生Service调DAO时，要创建DAO层的对象
}
