package com.example.springbootmybatisplus;

import com.example.springbootmybatisplus.entity.User;
import com.example.springbootmybatisplus.mapper.UserMapper;
import com.example.springbootmybatisplus.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MybatisPlusTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @Before
    public void doBefore() {
        userMapper.delete(null);
        User user1 = new User().setUsername("admin").setName("系统管理员")
                .setPassword("123456").setSalt("123").setPhone("10086").setTips("系统管理员").setState(true);
        User user2 = new User().setUsername("zhangsan").setName("张三")
                .setPassword("123456").setSalt("456").setPhone("10010").setTips("超级管理员").setState(true);
        userMapper.insert(user1);
        userMapper.insert(user2);
    }

    @Test
    public void test1() {
        User user = new User()
                .setUsername("xiaoxx")
                .setName("小星星")
                .setPassword("222222")
                .setPhone("13890907676");
        // 自定义的userService方法
        userService.insertUser(user);
        // userService.save(user);

        User user1 = userService.getById(user.getId());
        assertThat(user1.getUsername(), equalTo("xiaoxx"));

        boolean flag = userService.removeById(user.getId());
        Assert.assertTrue(flag);
    }

    @Test
    public void test2() {
        Map<String, Object> params = new HashMap<>();
    }
}
