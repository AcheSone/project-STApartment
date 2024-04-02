package com.baidu.crud.service;

import com.baidu.crud.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

@Service
public interface UserService extends IService<User> {
    //必须指定类型为实体类，相当于原生Service调DAO时，要创建DAO层的对象
}
