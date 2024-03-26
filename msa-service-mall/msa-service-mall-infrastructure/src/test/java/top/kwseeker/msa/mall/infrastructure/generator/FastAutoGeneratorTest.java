package top.kwseeker.msa.mall.infrastructure.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.junit.jupiter.api.Test;

import java.sql.Types;
import java.util.Collections;

/**
 * MybatisPlus新代码生成层器自动生成DAO层代码
 * <a href="https://baomidou.com/pages/981406/#%E6%95%B0%E6%8D%AE%E5%BA%93%E9%85%8D%E7%BD%AE-datasourceconfig">代码生成器配置新</a>
 */
public class FastAutoGeneratorTest {

    @Test
    public void testGenerateDAOCode() {
        DataSourceConfig.Builder dataSourceConfigBuilder = new DataSourceConfig.Builder("jdbc:mysql://localhost:3306/msa-action?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC",
                "root", "123456")
                .typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                    int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                    if (typeCode == Types.SMALLINT) {
                        // 自定义类型转换
                        return DbColumnType.INTEGER;
                    }
                    return typeRegistry.getColumnType(metaInfo);
                });

        FastAutoGenerator.create(dataSourceConfigBuilder)
                .globalConfig(builder -> {
                    builder.author("kwseeker") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            //.fileOverride() // 覆盖已生成文件
                            .outputDir("/home/arvin/mywork/java/micro_service/msa-action/msa-service-mall/msa-service-mall-infrastructure/src/main/java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("top.kwseeker.msa.mall.infrastructure.dao") // 设置父包名
                            //.moduleName("msa-service-mall-infrastructure") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "/home/arvin/mywork/java/micro_service/msa-action/msa-service-mall/msa-service-mall-infrastructure/src/main/java")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("md_activity", "md_activity_setting", "md_item", "md_user_activity_record", "md_user_item") // 设置需要生成的表名
                            .addTablePrefix("md_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
