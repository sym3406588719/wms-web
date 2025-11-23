package com.wms.msc.common;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 适配多模块项目的 MyBatis-Plus 3.4.0 代码生成器
 */
class FinalCodeGenerator {
    // -------------------------- 数据库与包配置 --------------------------
    private static final String DB_URL = "jdbc:mysql://localhost:3306/wms02?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=GMT%2B8";
    private static final String DB_USER = "root";
    private static final String DB_PWD = "123456";
    private static final String PARENT_PACKAGE = "com.wms.msc";
    private static final String AUTHOR = "wms";
    // ----------------------------------------------------------------------

    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入" + tip + "（多个表用英文逗号分割）：");
        if (scanner.hasNext()) {
            String input = scanner.next().trim();
            if (StringUtils.isNotBlank(input)) {
                return input;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        AutoGenerator mpg = new AutoGenerator();

        // 关键修改：指定子模块 msc 的路径（父项目根路径 + /msc）
        String projectPath = System.getProperty("user.dir") + "/msc";

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(projectPath + "/src/main/java"); // 代码输出到 msc 的 src/main/java
        gc.setAuthor(AUTHOR);
        gc.setOpen(false);
        gc.setSwagger2(true);
        gc.setServiceName("%sService");
        gc.setBaseResultMap(true);
        gc.setBaseColumnList(true);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(DB_URL);
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername(DB_USER);
        dsc.setPassword(DB_PWD);
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(PARENT_PACKAGE);
        pc.setEntity("entity");
        pc.setMapper("mapper");
        pc.setService("service");
        pc.setServiceImpl("service.impl");
        pc.setController("controller");
        mpg.setPackageInfo(pc);

        // 自定义 XML 配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {}
        };
        List<FileOutConfig> focList = new ArrayList<>();
        String xmlTemplatePath = "templates/mapper.xml.ftl";
        focList.add(new FileOutConfig(xmlTemplatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/src/main/resources/mapper/"
                        + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 模板配置
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setInclude(scanner("表名").split(","));
        strategy.setControllerMappingHyphenStyle(true);
        mpg.setStrategy(strategy);

        // 模板引擎
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        // 执行生成
        mpg.execute();
        System.out.println("✅ 代码生成完成！");
        System.out.println("代码路径：" + gc.getOutputDir());
        System.out.println("XML 路径：" + projectPath + "/src/main/resources/mapper");
    }
}


