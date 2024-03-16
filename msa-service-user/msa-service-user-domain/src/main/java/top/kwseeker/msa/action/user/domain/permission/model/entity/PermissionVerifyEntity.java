package top.kwseeker.msa.action.user.domain.permission.model.entity;

import lombok.Builder;
import lombok.Data;

/**
 * 通常是 sub obj act
 */
@Data
@Builder
public class PermissionVerifyEntity {

    private String userId;
    private String sub;
    private String obj;
    private String[] acts;
}
