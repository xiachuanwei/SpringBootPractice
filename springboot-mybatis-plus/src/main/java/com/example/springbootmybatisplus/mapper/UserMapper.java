package com.example.springbootmybatisplus.mapper;

import com.example.springbootmybatisplus.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 后台管理用户表 Mapper 接口
 * </p>
 *
 * @author xww
 * @since 2019-11-11
 */
@Component
public interface UserMapper extends BaseMapper<User> {

}
