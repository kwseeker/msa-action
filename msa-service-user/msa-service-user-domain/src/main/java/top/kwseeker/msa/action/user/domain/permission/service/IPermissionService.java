package top.kwseeker.msa.action.user.domain.permission.service;

import top.kwseeker.msa.action.user.domain.permission.model.entity.PermissionVerifyEntity;

import java.util.Set;

public interface IPermissionService {

    /**
     * 查询用户拥有的所有权限
     */
    Set<String> getAllPermissions(Long userId);

    /**
     * 查询用户拥有的所有角色
     */
    Set<String> getAllRoles(Long userId);

    /**
     * 支持 Casbin 权限校验
     */
    boolean verifyPermission(PermissionVerifyEntity permissionVerifyEntity);
}
