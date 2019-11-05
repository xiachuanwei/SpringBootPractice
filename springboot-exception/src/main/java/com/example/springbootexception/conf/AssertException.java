package com.example.springbootexception.conf;

import lombok.Getter;

/**
 * 自定义的断言异常 AssertException
 */
@Getter
public class AssertException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    /**
     * 返回码
     */
    private IExceptionEnum exceptionEnum;
    /**
     * 异常消息参数
     */
    private Object[] args;

    /**
     * 通过枚举类信息初始化异常
     */
    public AssertException(IExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMessage());
        this.exceptionEnum = exceptionEnum;
    }

    /**
     * 通过异常码和异常信息 匿名内部类初始化异常
     *
     * @param code 异常码
     * @param msg  异常信息
     */
    public AssertException(int code, String msg) {
        super(msg);
        this.exceptionEnum = new IExceptionEnum() {
            @Override
            public int getCode() {
                return code;
            }

            @Override
            public String getMessage() {
                return msg;
            }
        };
    }


    /**
     * @param exceptionEnum 异常枚举类
     * @param args          参数列表
     * @param message       信息
     */
    public AssertException(IExceptionEnum exceptionEnum, Object[] args, String message) {
        super(message);
        this.exceptionEnum = exceptionEnum;
        this.args = args;
    }

    /**
     * @param exceptionEnum 异常枚举类
     * @param args          参数列表
     * @param message       信息
     * @param cause         具体异常
     */
    public AssertException(IExceptionEnum exceptionEnum, Object[] args, String message, Throwable cause) {
        super(message, cause);
        this.exceptionEnum = exceptionEnum;
        this.args = args;
    }
}
