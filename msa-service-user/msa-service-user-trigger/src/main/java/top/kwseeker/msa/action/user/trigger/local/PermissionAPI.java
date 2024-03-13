package top.kwseeker.msa.action.user.trigger.local;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.kwseeker.msa.action.user.api.IPermissionAPI;
import top.kwseeker.msa.action.user.domain.permission.service.IPermissionService;

import javax.annotation.Resource;
import java.util.Set;

@Slf4j
@Service
public class PermissionAPI implements IPermissionAPI {

    @Resource
    private IPermissionService permissionService;

    @Override
    public boolean hasAnyPermissions(Long userId, String... permissions) {
        Set<String> userPermissions = permissionService.getAllPermissions(userId);
        for (String permission : permissions) {
            boolean contains = userPermissions.contains(permission);
            if (contains) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasAnyRoles(Long userId, String... roles) {
        Set<String> userRoles = permissionService.getAllRoles(userId);
        for (String role : roles) {
            boolean contains = userRoles.contains(role);
            if (contains) {
                return true;
            }
        }
        return false;
    }
}
