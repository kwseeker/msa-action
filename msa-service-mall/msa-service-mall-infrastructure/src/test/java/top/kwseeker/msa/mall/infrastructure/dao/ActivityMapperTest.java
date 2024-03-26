package top.kwseeker.msa.mall.infrastructure.dao;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import top.kwseeker.msa.mall.infrastructure.po.ActivityPO;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@MybatisPlusTest    //默认使用h2
@SpringBootTest(classes = MockApplication.class)
//测试期间不替换任何数据库配置，而是使用项目的实际数据库配置
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//配置测试数据源
@TestPropertySource(properties = {
        "spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver",
        "spring.datasource.url=jdbc:mysql://localhost:3306/msa-action?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC",
        "spring.datasource.username=root",
        "spring.datasource.password=123456",
})
class ActivityMapperTest {

    @Resource
    private ActivityMapper activityMapper;

    //@BeforeAll
    //static void setupClass(@Autowired DataSource dataSource) throws Exception {
    //    try (Connection conn = dataSource.getConnection()) {
    //        //ScriptUtils.executeSqlScript(conn, new ClassPathResource("/sql/test_schema.sql"));
    //        //ScriptUtils.executeSqlScript(conn, new ClassPathResource("/sql/test_data.sql"));
    //        ScriptUtils.executeSqlScript(conn, new FileUrlResource("/home/arvin/mywork/java/micro_service/msa-action/deploy/sql/mall-domain.sql"));
    //    }
    //}

    @Test
    public void testSelect() {
        ActivityPO activityPO = activityMapper.selectById("1");
        assertEquals("测试活动A", activityPO.getName());
    }
}