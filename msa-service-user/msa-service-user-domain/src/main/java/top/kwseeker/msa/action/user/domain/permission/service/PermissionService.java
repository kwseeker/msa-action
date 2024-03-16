package top.kwseeker.msa.action.user.domain.permission.service;

import org.casbin.jcasbin.main.Enforcer;
import org.springframework.stereotype.Service;
import top.kwseeker.msa.action.user.domain.permission.model.entity.MenuEntity;
import top.kwseeker.msa.action.user.domain.permission.model.entity.PermissionVerifyEntity;
import top.kwseeker.msa.action.user.domain.permission.model.entity.RoleEntity;
import top.kwseeker.msa.action.user.domain.permission.repository.IPermissionRepository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PermissionService implements IPermissionService {

    @Resource
    private IPermissionRepository permissionRepository;
    @Resource
    private Enforcer enforcer;

    /**
     * userId -> roles -> permissions
     */
    @Override
    public Set<String> getAllPermissions(Long userId) {
        List<MenuEntity> allPermissions = permissionRepository.getAllPermissions(userId);
        return allPermissions.stream().map(MenuEntity::getPermission).collect(Collectors.toSet());
    }

    /**
     * userId -> roles
     */
    @Override
    public Set<String> getAllRoles(Long userId) {
        List<RoleEntity> allRoles = permissionRepository.getAllRoles(userId);
        return allRoles.stream().map(RoleEntity::getCode).collect(Collectors.toSet());
    }

    @Override
    public boolean verifyPermission(PermissionVerifyEntity permissionVerifyEntity) {

        List<List<String>> rules = new ArrayList<>(permissionVerifyEntity.getActs().length);
        String sub = permissionVerifyEntity.getSub();
        String obj = permissionVerifyEntity.getObj();
        String[] acts = permissionVerifyEntity.getActs();
        for (String act : acts) {
            rules.add(Arrays.asList(sub, obj, act));
        }
        List<Boolean> results = enforcer.batchEnforce(rules);
        return results.contains(true);
    }

    // 权限管理接口，待添加，
    // 包括自定义RBAC权限管理、Casbin策略模型权限管理
}
