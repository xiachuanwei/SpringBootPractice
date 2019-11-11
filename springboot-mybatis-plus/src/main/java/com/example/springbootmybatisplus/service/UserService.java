package com.example.springbootmybatisplus.service;

import com.example.springbootmybatisplus.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 后台管理用户表 服务类
 * </p>
 *
 * @author xww
 * @since 2019-11-11
 */
public interface UserService extends IService<User> {
    /**
     * 新增用户
     *
     * @param user 用户信息
     */
    public void insertUser(User user);

}
