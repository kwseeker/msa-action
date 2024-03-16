package top.kwseeker.msa.action.user.api.model;

import lombok.Builder;
import lombok.Data;

/**
 * 权限校验信息，用于支持 Casbin Enforcer 权限校验
 */
@Data
@Builder
public class PermissionVerifyDTO {

    private Long userId;
    //比如用户名
    private String sub;
    //比如请求路径
    private String obj;
    //比如权限要求
    private String[] acts;
}
