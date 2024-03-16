package top.kwseeker.msa.action.user.domain.permission.service;

import org.casbin.jcasbin.main.Enforcer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PermissionServiceTest {

    @Resource
    private Enforcer enforcer;

    @Test
    public void testEnforce() {
        boolean enforce1 = enforcer.enforce("admin", "/user/manage/create", "system:user:create");
        boolean enforce11 = enforcer.enforce("admin", "/user/manage/create", "(system:user:create)");   //enforce接口中act参数不支持正则表达式
        boolean enforce2 = enforcer.enforce("kwseeker", "/user/manage/create", "system:user:create");
        assertTrue(enforce1);
        assertFalse(enforce11);
        assertFalse(enforce2);
    }
}
