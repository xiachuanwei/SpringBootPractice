package com.example.springbootexception.conf;

import java.text.MessageFormat;

/**
 * 自定义的断言
 * 可根据业务增加断言方法
 */
public interface Assert extends IExceptionEnum {
    /**
     * 断言对象为false，则抛出异常
     *
     * @param obj 待判断对象
     */
    default void isTrue(boolean obj, Object... args) {
        if (!obj) {
            throw newException(args);
        }
    }

    /**
     * 断言对象非空。如果对象为空，则抛出异常
     *
     * @param obj  待判断对象
     * @param args 参数
     */
    default void notNull(Object obj, Object... args) {
        if (obj == null) {
            throw newException(args);
        }
    }

    /**
     * 抛出异常的方法
     */
    default AssertException newException(Object... args) {
        // 格式化IExceptionEnum枚举对象的message信息
        String msg = MessageFormat.format(this.getMessage(), args);
        // 返回Exception对象
        return new AssertException(this, args, msg);
    }

    /**
     * 抛出异常的方法
     */
    default AssertException newException(Throwable t, Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);
        return new AssertException(this, args, msg, t);
    }

}
