package com.example.springbootmybatisplus;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.junit.jupiter.api.Test;

/**
 * 代码自动生成器测试
 */
public class MybatisPlusCodeGeneratorTest {

    @Test
    public void autoGenerator() {
        // 代码生成器
        AutoGenerator autoGenerator = new AutoGenerator();
        // 配置模板引擎
        autoGenerator.setTemplateEngine(new FreemarkerTemplateEngine());

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(System.getProperty("user.dir") + "/src/main/java");
        gc.setAuthor("xww");
        gc.setOpen(false);
        gc.setSwagger2(true);
        gc.setMapperName("%sMapper");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%ServiceImpl");
        gc.setFileOverride(true);

        gc.setBaseResultMap(true);
        gc.setBaseColumnList(true);
        autoGenerator.setGlobalConfig(gc);

        // 数据源配置，指定需要生成代码的具体数据库
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&useSSL=false&characterEncoding=utf8");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("root");
        autoGenerator.setDataSource(dsc);

        // 数据库表配置，生成哪些表或者排除哪些表/父类Super对象/类或字段前缀等
        StrategyConfig strategy = new StrategyConfig();
        strategy.setInclude("t_user");
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix("t_");
        // 如果担心mp的功能与业务混淆，可以配置service不继承baseService
        // strategy.setSuperServiceClass(null);
        // strategy.setSuperServiceImplClass(null);
        autoGenerator.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.example.springbootmybatisplus");
        autoGenerator.setPackageInfo(pc);

        // 模板配置，默认即可 entity/mapper/service/serviceImpl/controller
        TemplateConfig templateConfig = new TemplateConfig();
        autoGenerator.setTemplate(templateConfig);


        // 自定义配置 不常用
        // 1.自定义返回配置 Map 对象，后面使用
        // 2.自定义模板并输出文件 FileOutConfig
        // 3.自定义创建/覆盖文件等
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        autoGenerator.setCfg(cfg);

        // 执行
        autoGenerator.execute();
    }
}
