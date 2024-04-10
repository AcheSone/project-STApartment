package com.baidu.lease.common.exception;

import com.baidu.lease.common.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail();
    }

    //这里进行自定义异常的拦截声明，自定义异常引用了响应状态表，的数据，能清晰的向前端响应错误信息
    //即，LeaseException中，将传入的异常状态响应表对象中的响应码和信息给予自己引用，
    @ExceptionHandler(LeaseException.class)
    public Result error(LeaseException e){
        e.printStackTrace();
        return Result.fail(e.getCode(), e.getMessage());
    };

}
