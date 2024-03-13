package top.kwseeker.msa.action.user.infrastructure.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import top.kwseeker.msa.action.user.domain.permission.model.entity.MenuEntity;
import top.kwseeker.msa.action.user.domain.permission.model.entity.RoleEntity;
import top.kwseeker.msa.action.user.domain.permission.repository.IPermissionRepository;
import top.kwseeker.msa.action.user.infrastructure.converter.PermissionConverter;
import top.kwseeker.msa.action.user.infrastructure.dao.*;
import top.kwseeker.msa.action.user.infrastructure.po.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ServiceImpl 貌似不适合用在 DDD 中
 */
@Slf4j
@Repository
//public class PermissionRepository extends ServiceImpl<RoleMapper, RolePO> implements IPermissionRepository {
public class PermissionRepository implements IPermissionRepository {

    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private RoleMenuMapper roleMenuMapper;
    @Resource
    private MenuMapper menuMapper;

    @Override
    public List<MenuEntity> getAllPermissions(Long userId) {
        List<UserRolePO> userRolePOs = userRoleMapper.selectList(UserRolePO::getUserId, userId);
        List<Long> roleIds = userRolePOs.stream().map(UserRolePO::getRoleId).collect(Collectors.toList());
        List<RoleMenuPO> roleMenuPOs = roleMenuMapper.selectListByMulti(RoleMenuPO::getRoleId, roleIds);
        List<Long> menuIds = roleMenuPOs.stream().map(RoleMenuPO::getMenuId).collect(Collectors.toList());
        List<MenuPO> menuPOs = menuMapper.selectListByMulti(MenuPO::getId, menuIds);
        return menuPOs.stream().map(PermissionConverter.INSTANCE::convert).collect(Collectors.toList());
    }

    @Override
    public List<RoleEntity> getAllRoles(Long userId) {
        List<UserRolePO> userRolePOs = userRoleMapper.selectList(UserRolePO::getUserId, userId);
        List<Long> roleIds = userRolePOs.stream().map(UserRolePO::getRoleId).collect(Collectors.toList());
        List<RolePO> rolePOs = roleMapper.selectList(RolePO::getId, roleIds);
        return rolePOs.stream().map(PermissionConverter.INSTANCE::convert).collect(Collectors.toList());
    }
}
