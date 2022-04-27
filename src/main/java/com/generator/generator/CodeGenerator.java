package com.generator.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.BeetlTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.io.File;
import java.time.LocalDate;
import java.util.*;

/**
 * 代码生成器
 * https://github.com/baomidou/generator
 *
 * @author: LC
 * @date 2022/3/1 5:01 下午
 * @ClassName: CodeGenerator
 */
public class CodeGenerator {


    public static void main(String[] args) {
        String url = "jdbc:mysql://127.0.0.1:3306/seckill?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
        String userName = "root";
        String passWord = "123";

        //删除现有文件夹下内容
        String outputDirStr = System.getProperty("user.dir") + "/src/main/java";
        String singletonMapStr = System.getProperty("user.dir") + "/src/main/resources/mapper";
        String parentStr = "com.example.cseckill";
        File outputDirFile = new File(outputDirStr + File.separator + parentStr.replace(".", File.separator));
        if (outputDirFile.exists() && outputDirFile.isDirectory()) {
            deleteDirectory(outputDirStr + File.separator + parentStr.replace(".", File.separator));
        }
        File singletonMapFile = new File(singletonMapStr);
        if (singletonMapFile.exists() && singletonMapFile.isDirectory()) {
            deleteDirectory(singletonMapStr);
        }


        FastAutoGenerator.create(url, userName, passWord)
                // 全局配置 文件作者名称
                .globalConfig((scanner, builder) ->
                        builder.author("chaos") //设置作者
                                .enableSwagger() //开启swagger
                                .commentDate((LocalDate.now()) + "") //注释日期
                                .fileOverride()//覆盖以生成文件
                                .dateType(DateType.ONLY_DATE) //时间策略 entity 类中使用Date	DateType.ONLY_DATE 默认值: DateType.TIME_PACK
                                .outputDir(outputDirStr) //指定输出目录
                )
                // 包配置
                .packageConfig((scanner, builder) ->
                        builder.parent(parentStr) //设置父包名
                                //  .moduleName("system") // 设置父包模块名
                                .pathInfo(Collections.singletonMap(OutputFile.mapperXml, singletonMapStr)) // 设置mapperXml生成路径
                )
                // 策略配置
                .strategyConfig((scanner, builder) ->
//                                builder.addInclude(getTables("t_goods,t_order,t_seckill_goods,t_seckill_order"))//设置要生成的表名
                                builder.addInclude(getTables("t_user"))//设置要生成的表名
                                        .controllerBuilder()
                                        .enableRestStyle()
                                        .enableHyphenStyle()
                                        .entityBuilder()
//                                        .enableLombok() //生成Lombok注解
//                                .addTableFills(new Column("create_time", FieldFill.INSERT))
                                        .build()
                                        .mapperBuilder()
                                        .enableBaseResultMap()
                                        .build()
                )
                //生成时不使用表前缀
                .strategyConfig((scanner, builder) -> builder.addTablePrefix("t_").build())
                //模版配置
                .templateConfig(
                        (scanner, builder) ->
                                builder.disable(TemplateType.ENTITY)
                                        .entity("/templates/vm/entity.java")
                                        .service("/templates/vm/service.java")
                                        .serviceImpl("/templates/vm/serviceImpl.java")
                                        .mapper("/templates/vm/mapper.java")
                                        .mapperXml("/templates/vm/mapper.xml")
                                        .controller("/templates/vm/controller.java")
                                        .build()
                )
                /*
                    模板引擎配置，默认 Velocity 可选模板引擎 Beetl 或 Freemarker
                    .templateEngine(new BeetlTemplateEngine())
                   .templateEngine(new FreemarkerTemplateEngine())
                 */
//                .templateEngine(new FreemarkerTemplateEngine())
//                .templateEngine(new BeetlTemplateEngine())
                .execute();
    }


    // 处理 all 情况
    protected static List<String> getTables(String tables) {
        return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
    }

    /**
     * 删除单个文件
     *
     * @param fileName：要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dir：要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            System.out.println("删除目录失败：" + dir + "不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            System.out.println("删除目录失败！");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            System.out.println("删除目录" + dir + "成功！");
            return true;
        } else {
            return false;
        }
    }


}
