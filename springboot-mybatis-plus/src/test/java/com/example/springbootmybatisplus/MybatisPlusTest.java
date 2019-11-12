package com.example.springbootmybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springbootmybatisplus.entity.User;
import com.example.springbootmybatisplus.mapper.UserMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 从mp示例上复制的
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisPlusTest {

    @Resource
    private UserMapper mapper;

    @After
    public void doAfter() {
        mapper.delete(null);
    }

    @Test
    public void doInsert() {
        User user = new User().setId(10086).setName("tom").setAge(3).setEmail("abc@mp.com");
        assertThat(mapper.insert(user)).isGreaterThan(0);
        assertThat(user.getId()).isNotNull();
    }


    @Test
    public void doDelete() {
        mapper.insert(new User().setId(10086).setName("tom").setAge(3).setEmail("abc@mp.com"));
        assertThat(mapper.deleteById(10086)).isGreaterThan(0);

        mapper.insert(new User().setId(10086).setName("tom").setAge(3).setEmail("abc@mp.com"));
        assertThat(mapper.delete(new QueryWrapper<User>()
                .lambda().eq(User::getName, "tom"))).isGreaterThan(0);
    }


    @Test
    public void doUpdate() {
        mapper.insert(new User().setId(10086).setName("tom").setAge(3).setEmail("abc@mp.com"));
        assertThat(mapper.updateById(new User().setId(10086).setEmail("ab@c.c"))).isGreaterThan(0);

        assertThat(mapper.update(new User().setName("mp"),
                new UpdateWrapper<User>().lambda().eq(User::getAge, 3).eq(User::getId, 10086))).isGreaterThan(0);
        User user = mapper.selectById(10086);
        assertThat(user.getAge()).isEqualTo(3);
        assertThat(user.getName()).isEqualTo("mp");

        mapper.update(null, Wrappers.<User>lambdaUpdate().set(User::getEmail, null).eq(User::getId, 10086));
        assertThat(mapper.selectById(10086).getEmail()).isNull();
        assertThat(mapper.selectById(10086).getName()).isEqualTo("mp");
    }


    @Test
    public void dSelect() {
        mapper.insert(new User().setId(10086).setName("tom").setAge(3).setEmail("abc@mp.com"));

        assertThat(mapper.selectById(10086).getEmail()).isEqualTo("abc@mp.com");

        User user = mapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getId, 10086));
        assertThat(user.getName()).isEqualTo("tom");
        assertThat(user.getAge()).isEqualTo(3);

        mapper.selectList(Wrappers.<User>lambdaQuery().select(User::getId, User::getName))
                .forEach(x -> {
                    assertThat(x.getId()).isNotNull();
                    assertThat(x.getEmail()).isNull();
                    assertThat(x.getName()).isNotNull();
                    assertThat(x.getAge()).isNull();
                });
        mapper.selectList(new QueryWrapper<User>().select("id", "name"))
                .forEach(x -> {
                    assertThat(x.getId()).isNotNull();
                    assertThat(x.getEmail()).isNull();
                    assertThat(x.getName()).isNotNull();
                    assertThat(x.getAge()).isNull();
                });
    }

    @Test
    public void orderBy() {
        mapper.insert(new User().setId(10086).setName("tom").setAge(3).setEmail("abc@mp.com"));

        List<User> users = mapper.selectList(Wrappers.<User>query().orderByAsc("age"));
        assertThat(users).isNotEmpty();
    }

    @Test
    public void selectMaps() {
        mapper.insert(new User().setId(10086).setName("tom").setAge(3).setEmail("abc@mp.com"));

        List<Map<String, Object>> mapList = mapper.selectMaps(Wrappers.<User>query().orderByAsc("age"));
        assertThat(mapList).isNotEmpty();
        assertThat(mapList.get(0)).isNotEmpty();
        System.out.println(mapList.get(0));
    }

    @Test
    public void selectMapsPage() {
        mapper.insert(new User().setId(10086).setName("tom").setAge(3).setEmail("abc@mp.com"));

        IPage<Map<String, Object>> page = mapper.selectMapsPage(new Page<>(1, 5), Wrappers.<User>query().orderByAsc("age"));
        assertThat(page).isNotNull();
        assertThat(page.getRecords()).isNotEmpty();
        assertThat(page.getRecords().get(0)).isNotEmpty();
        System.out.println(page.getRecords().get(0));
    }

    @Test
    public void orderByLambda() {
        mapper.insert(new User().setId(10086).setName("tom").setAge(3).setEmail("abc@mp.com"));

        List<User> users = mapper.selectList(Wrappers.<User>lambdaQuery().orderByAsc(User::getAge));
        assertThat(users).isNotEmpty();
    }

    @Test
    public void testSelectMaxId() {
        mapper.insert(new User().setId(10086).setName("tom").setAge(3).setEmail("abc@mp.com"));

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("max(id) as id");
        User user = mapper.selectOne(wrapper);
        System.out.println("maxId=" + user.getId());
        List<User> users = mapper.selectList(Wrappers.<User>lambdaQuery().orderByDesc(User::getId));
        Assert.assertEquals(user.getId().longValue(), users.get(0).getId().longValue());
    }

    @Test
    public void testGroup() {
        mapper.insert(new User().setId(10086).setName("tom").setAge(3).setEmail("abc@mp.com"));

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("age, count(*)")
                .groupBy("age");
        List<Map<String, Object>> maplist = mapper.selectMaps(wrapper);
        for (Map<String, Object> mp : maplist) {
            System.out.println(mp);
        }
        /**
         * lambdaQueryWrapper groupBy orderBy
         */
        LambdaQueryWrapper<User> lambdaQueryWrapper = new QueryWrapper<User>().lambda()
                .select(User::getAge)
                .groupBy(User::getAge)
                .orderByAsc(User::getAge);
        for (User user : mapper.selectList(lambdaQueryWrapper)) {
            System.out.println(user);
        }
    }

}
