package top.kwseeker.msa.action.user.infrastructure.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.kwseeker.msa.action.user.infrastructure.po.UserPO;
import top.kwseeker.msa.action.user.types.enums.Sex;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@MybatisPlusTest
@SpringBootTest
public class IUserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        UserPO userPO = userMapper.selectByUsername("kwseeker");
        assertEquals(11L, userPO.getId());
    }

    @Test
    public void testInsert() {
        UserPO userPO = UserPO.builder()
                .username("tester")
                .password("123456")
                .nickname("tester")
                .remark("测试用户")
                .deptId(1L)
                .email("tester@gmail.com")
                .mobile("13711113333")
                .sex(Sex.MALE.getSex())
                .build();
        int count = userMapper.insert(userPO);
        assertEquals(1, count);
    }
}