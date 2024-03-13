package top.kwseeker.msa.action.user.domain.permission.service;

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
}
