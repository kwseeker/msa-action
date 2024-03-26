package top.kwseeker.msa.action.security.core.permission;

import cn.hutool.core.collection.CollUtil;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import top.kwseeker.msa.action.framework.common.core.KeyValue;
import top.kwseeker.msa.action.framework.common.util.CacheUtil;
import top.kwseeker.msa.action.security.core.LoginUser;
import top.kwseeker.msa.action.security.core.RequestInfo;
import top.kwseeker.msa.action.security.core.util.SecurityFrameworkUtil;
import top.kwseeker.msa.action.user.api.feign.PermissionAPIClient;
import top.kwseeker.msa.action.user.api.local.IPermissionAPI;
import top.kwseeker.msa.action.user.api.model.PermissionVerifyDTO;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * 自定义鉴权服务
 */
@AllArgsConstructor
public class DefaultPermissionVerifier implements IPermissionVerifier {

    private IPermissionAPI permissionService;
    private PermissionAPIClient permissionAPIClient;    //TODO

    /**
     * 针对 {@link #hasAnyRoles(String...)} 的缓存
     * 权限基本很少改动，比较适合添加缓存，提升性能
     */
    private final LoadingCache<KeyValue<Long, List<String>>, Boolean> hasAnyRolesCache = CacheUtil.buildAsyncReloadingCache(
            Duration.ofMinutes(1L), // 过期时间 1 分钟
            new CacheLoader<KeyValue<Long, List<String>>, Boolean>() {
                @Override
                public Boolean load(KeyValue<Long, List<String>> key) {
                    return permissionService.hasAnyRoles(key.getKey(), key.getValue().toArray(new String[0]));
                }
            });

    /**
     * 针对 {@link #hasAnyPermissions(String...)} 的缓存
     */
    private final LoadingCache<KeyValue<Long, List<String>>, Boolean> hasAnyPermissionsCache = CacheUtil.buildAsyncReloadingCache(
            Duration.ofMinutes(1L), // 过期时间 1 分钟
            new CacheLoader<KeyValue<Long, List<String>>, Boolean>() {
                @Override
                public Boolean load(KeyValue<Long, List<String>> key) {
                    return permissionService.hasAnyPermissions(key.getKey(), key.getValue().toArray(new String[0]));
                }
            });

    @Override
    public boolean hasPermission(String permission) {
        return hasAnyPermissions(permission);
    }

    @Override
    @SneakyThrows
    public boolean hasAnyPermissions(String... permissions) {
        return hasAnyPermissionsCache.get(new KeyValue<>(SecurityFrameworkUtil.getLoginUserId(), Arrays.asList(permissions)));
    }

    @Override
    public boolean hasRole(String role) {
        return hasAnyRoles(role);
    }

    @Override
    @SneakyThrows
    public boolean hasAnyRoles(String... roles) {
        return hasAnyRolesCache.get(new KeyValue<>(SecurityFrameworkUtil.getLoginUserId(), Arrays.asList(roles)));
    }

    @Override
    public boolean hasScope(String scope) {
        return hasAnyScopes(scope);
    }

    @Override
    public boolean hasAnyScopes(String... scope) {
        LoginUser user = SecurityFrameworkUtil.getLoginUser();
        if (user == null) {
            return false;
        }
        return CollUtil.containsAny(user.getScopes(), Arrays.asList(scope));
    }

    @Override
    public boolean verifyPerms(String... permissions) {
        RequestInfo requestInfo = SecurityFrameworkUtil.getRequestInfo();
        if (requestInfo == null || requestInfo.getLoginUser() == null) {
            return false;
        }

        LoginUser loginUser = requestInfo.getLoginUser();
        PermissionVerifyDTO permissionVerifyDTO = PermissionVerifyDTO.builder()
                .userId(loginUser.getId())
                .sub(loginUser.getUsername())    //用户username
                .obj(requestInfo.getRequestDetails().getUri()) //请求路径
                .acts(permissions)
                .build();
        return permissionService.verify(permissionVerifyDTO);
    }
}
