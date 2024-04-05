package com.baidu.lease.web.admin.custom.converter;

import com.baidu.lease.model.enums.BaseEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

@Component
public class StringToBaseEnumConverterFactory implements ConverterFactory<String, BaseEnum> {
    @Override
    public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
        Converter<String, T> converter = new Converter<String, T>() {
            @Override
            public T convert(String code) {

                //通过反射获取targetType对象中的枚举对象，并且遍历所有，
                //若是有一个符合则返回该对象给匿名对象的调用方法
                for (T enumConstant : targetType.getEnumConstants()) {
                    if (enumConstant.getCode().equals(Integer.valueOf(code))){
                        return enumConstant;
                    }
                }
                throw new IllegalArgumentException("非法的枚举值："+ code);
            }
        };
        //匿名对象的调用方法将该枚举对象返回给前端
        return converter;
    }
}
