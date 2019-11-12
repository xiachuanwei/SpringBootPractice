package com.example.springbootmybatisplus.mapper;

import com.example.springbootmybatisplus.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author xww
 * @since 2019-11-12
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
