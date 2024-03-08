package top.kwseeker.msa.action.user.trigger.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kwseeker.msa.action.user.domain.auth.entity.LoginRespEntity;
import top.kwseeker.msa.action.framework.common.model.Response;
import top.kwseeker.msa.action.user.trigger.http.dto.LoginDTO;
import top.kwseeker.msa.action.user.types.common.UserErrorCodes;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public Response<LoginRespEntity> login(@RequestBody LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        if (("admin".equalsIgnoreCase(username) && "admin".equals(password)) ||
                ("kwseeker".equalsIgnoreCase(username) && "123456".equals(password))) {
            //登录成功
            LoginRespEntity loginRespEntity = LoginRespEntity.builder()
                    .userId(10000L)
                    .accessToken("an-access-token")
                    .build();
            return Response.success(loginRespEntity);
        }
        return Response.fail(UserErrorCodes.INVALID_USERNAME_OR_PASSWORD);
    }
}
