package com.baidu.lease.web.admin.custom2.converter;

import com.baidu.lease.model.enums.ItemType;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
//ItemType是我们要拿code进行对比的枚举类对象的类型
public class StringToItemTypeConverter implements Converter<String, ItemType> {

    //通过实现该方法，SpringMVC此时拥有了一个ItemType类型的code比较器，（用来对应code码相应的枚举对象）
    @Override
    public ItemType convert(String code) {
        for (ItemType value : ItemType.values()){
            //获取该对象的值，与前端的传入的实质为int的String类型标签码code进行比较
            if (value.getCode().equals(Integer.valueOf(code))){
                //成功返回该枚举类对象给后端
                return value;
            }
        }
        //不成功则抛异常
        throw new IllegalArgumentException("code非法");
    }
}
