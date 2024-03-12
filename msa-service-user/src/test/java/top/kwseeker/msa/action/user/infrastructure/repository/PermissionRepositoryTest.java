package top.kwseeker.msa.action.user.infrastructure.repository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.kwseeker.msa.action.user.domain.permission.model.entity.MenuEntity;
import top.kwseeker.msa.action.user.domain.permission.repository.IPermissionRepository;
import top.kwseeker.msa.action.user.domain.user.repository.IUserRepository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PermissionRepositoryTest {

    @Resource
    private IPermissionRepository permissionRepository;

    @Test
    public void test() {
        List<MenuEntity> allPermissions = permissionRepository.getAllPermissions(11L);
        Set<String> collect = allPermissions.stream().map(MenuEntity::getPermission).collect(Collectors.toSet());
        System.out.println(collect);
    }
}