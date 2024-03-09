package top.kwseeker.msa.action.user.infrastructure.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.kwseeker.msa.action.user.infrastructure.po.UserPO;

import static org.junit.jupiter.api.Assertions.*;

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
}