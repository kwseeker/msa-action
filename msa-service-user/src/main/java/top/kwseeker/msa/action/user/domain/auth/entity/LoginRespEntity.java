package top.kwseeker.msa.action.user.domain.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录返回数据
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRespEntity {

    //用户ID
    private Long userId;
    //访问令牌
    private String accessToken;

}
