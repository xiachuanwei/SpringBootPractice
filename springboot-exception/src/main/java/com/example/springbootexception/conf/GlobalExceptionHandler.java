package com.example.springbootexception.conf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 全局异常处理
 */
@ControllerAdvice
@ResponseBody
@Slf4j
@Component
public class GlobalExceptionHandler {

    @Data
    @AllArgsConstructor
    class ErrorResponse {
        int code;
        String message;
    }

    /**
     * Controller上一层相关异常
     */
    @ExceptionHandler({
            NoHandlerFoundException.class, // 404异常
            HttpRequestMethodNotSupportedException.class, // 无对应HTTP方法的异常，比如定义了GET 用POST
            HttpMediaTypeNotSupportedException.class, //参数媒体类型不对
            MissingPathVariableException.class, // 缺少路径参数异常
            MissingServletRequestParameterException.class, // 缺少请求参数异常
            TypeMismatchException.class, // 参数类型匹配异常
            HttpMessageNotReadableException.class, // 参数不可读异常，与HttpMediaTypeNotSupportedException有些相反
            HttpMessageNotWritableException.class, // 一般返回数据序列化失败时抛出异常
            BindException.class, // 参数绑定异常
            MethodArgumentNotValidException.class,
            HttpMediaTypeNotAcceptableException.class,
            ServletRequestBindingException.class,
            ConversionNotSupportedException.class,
            MissingServletRequestPartException.class,
            AsyncRequestTimeoutException.class
    })
    @ResponseBody
    public ErrorResponse handleServletException(Exception e) {
        log.error(e.getMessage());
        int code = 500;
        return new ErrorResponse(code, e.getMessage());
    }

    /**
     * 基础异常
     */
    @ExceptionHandler(value = AssertException.class)
    @ResponseBody
    public ErrorResponse handleBaseException(AssertException e) {
        log.error(e.getMessage());
        return new ErrorResponse(e.getExceptionEnum().getCode(), e.getExceptionEnum().getMessage());
    }

    /**
     * 获取其它异常。包括500
     */
    @ExceptionHandler(value = Exception.class)
    public ErrorResponse defaultErrorHandler(Exception e) {
        log.error(e.getMessage());
        return new ErrorResponse(500, "服务异常，请联系管理员");
    }

}
