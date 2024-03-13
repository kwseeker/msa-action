package top.kwseeker.msa.action.user.domain.permission.repository;

import top.kwseeker.msa.action.user.domain.permission.model.entity.MenuEntity;
import top.kwseeker.msa.action.user.domain.permission.model.entity.RoleEntity;

import java.util.List;

public interface IPermissionRepository {

    /**
     * 查询用户拥有的所有权限
     */
    List<MenuEntity> getAllPermissions(Long userId);

    /**
     * 查询用户拥有的所有角色
     */
    List<RoleEntity> getAllRoles(Long userId);
}
