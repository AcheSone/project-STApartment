package com.baidu.crud.mapper;

import com.baidu.crud.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

public interface UserMapper extends BaseMapper<User> {
    //这里的泛型不能忘记，否则将无法得知传入的参数类型，mybatisplus提供的BaseMapper将无法进行类型推断

}




