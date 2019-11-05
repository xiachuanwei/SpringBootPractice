package com.example.springbootexception.conf;

/**
 * 异常返回码枚举接口
 */
public interface IExceptionEnum {
    /**
     * 获取返回码
     *
     * @return 返回码
     */
    int getCode();

    /**
     * 获取返回信息
     *
     * @return 返回信息
     */
    String getMessage();
}
