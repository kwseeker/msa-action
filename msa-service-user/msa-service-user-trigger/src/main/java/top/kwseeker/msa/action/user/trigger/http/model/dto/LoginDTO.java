package top.kwseeker.msa.action.user.trigger.http.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    private String username;
    private String password;

}
