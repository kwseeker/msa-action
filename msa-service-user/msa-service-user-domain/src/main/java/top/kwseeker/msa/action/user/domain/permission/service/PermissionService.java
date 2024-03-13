package top.kwseeker.msa.action.user.domain.permission.service;

import org.springframework.stereotype.Service;
import top.kwseeker.msa.action.user.domain.permission.model.entity.MenuEntity;
import top.kwseeker.msa.action.user.domain.permission.model.entity.RoleEntity;
import top.kwseeker.msa.action.user.domain.permission.repository.IPermissionRepository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PermissionService implements IPermissionService {

    @Resource
    private IPermissionRepository permissionRepository;

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
}
