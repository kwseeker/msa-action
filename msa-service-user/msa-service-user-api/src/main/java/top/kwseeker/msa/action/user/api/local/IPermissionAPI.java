package top.kwseeker.msa.action.user.api.local;

import top.kwseeker.msa.action.user.api.model.PermissionVerifyDTO;

/**
 * 权限检查服务
 */
public interface IPermissionAPI {

    /**
     * 判断用户是否有权限，任一一个即可
     * 查询用户权限表，获取所有权限信息，并比对
     *
     * @param userId      用户编号
     * @param permissions 权限
     * @return 是否
     */
    boolean hasAnyPermissions(Long userId, String... permissions);

    /**
     * 判断用户是否有角色，任一一个即可
     * 查询用户角色表，获取所有角色信息，并比对
     *
     * @param roles 角色数组
     * @return 是否
     */
    boolean hasAnyRoles(Long userId, String... roles);

    boolean verify(PermissionVerifyDTO permissionVerifyDTO);
}
