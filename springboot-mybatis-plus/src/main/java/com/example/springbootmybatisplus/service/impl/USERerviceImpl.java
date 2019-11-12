package com.example.springbootmybatisplus.service.impl;

import com.example.springbootmybatisplus.entity.User;
import com.example.springbootmybatisplus.mapper.UserMapper;
import com.example.springbootmybatisplus.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author xww
 * @since 2019-11-12
 */
@Service
public class USERerviceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
