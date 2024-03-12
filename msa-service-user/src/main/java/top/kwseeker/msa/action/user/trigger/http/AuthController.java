package top.kwseeker.msa.action.user.trigger.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kwseeker.msa.action.user.domain.auth.model.entity.LoginRespEntity;
import top.kwseeker.msa.action.framework.common.model.Response;
import top.kwseeker.msa.action.user.domain.auth.service.IAuthService;
import top.kwseeker.msa.action.user.trigger.http.dto.LoginDTO;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private IAuthService authService;

    @PermitAll
    @PostMapping("/login")
    public Response<LoginRespEntity> login(@RequestBody LoginDTO loginDTO) {
        LoginRespEntity loginResp = authService.login(loginDTO);
        return Response.success(loginResp);
    }
}
