package com.baidu.lease.common.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Date;

//此类用作配置自动填充的内容（配置的是BaseEntity中的插入和更新时间的属性）
@Component
public class MybatisMetaObjectHandler implements MetaObjectHandler {

    //为什么通过实现这个接口就可以进行配置？因为这是mybatis-plus提供的接口，与@Schema注解结合使用
    //分别为初次插入和更新时间做一个默认配置
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject,"createTime", Date.class,new Date());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject,"updateTime",Date.class,new Date());
    }
}
