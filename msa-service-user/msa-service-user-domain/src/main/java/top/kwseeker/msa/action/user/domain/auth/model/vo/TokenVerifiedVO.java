package top.kwseeker.msa.action.user.domain.auth.model.vo;

import lombok.Builder;
import lombok.Data;

/**
 * Token 校验后的返回值
 */
@Data
@Builder
public class TokenVerifiedVO {

    private Long userId;

}
