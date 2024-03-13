package top.kwseeker.msa.action.user.domain.auth.model.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginReqVO {

    private String username;
    private String password;
}
