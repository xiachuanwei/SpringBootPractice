# Mybatis-plus
#### 配置
pom.xml
```xml
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
        <version>3.2.0</version>
    </dependency>
```
application.yml
```yaml
mybatis-plus:
  mapper-locations: classpath:com/example/springbootmybatisplus/mapper/xml/*.xml
  type-aliases-package: classpath:com.example.springbootmybatisplus.entity
  configuration:
    map-underscore-to-camel-case: true
    default-statement-timeout: 30
    log-impl: org.apache.ibatis.logging.log4j2.Log4j2Impl
```
#### 优势
 mp对mybatis只做增强 不做改动 用起来不会对现有工程有影响
1. 继承BaseMapper，实现了对于单表的基本的CURD操作，并且有条件构造器方便查询
2. lambda方便编写查询条件
3. 支持Page分页
#### 主要的注解
* [@TableName](https://mybatis.plus/guide/annotation.html#tablename)
* [@TableField](https://mybatis.plus/guide/annotation.html#tablefield)
例子参考：MybatisPlusTest
#### AutoGenerator 代码生成器
针对MP的代码生成器，可以生成 Entity、Mapper、Mapper XML、Service、Controller的代码
例子参考：MybatisPlusCodeGeneratorTest
ps：生成的mapper中没有增删改查的，因为继承的BaseMapper已有默认实现