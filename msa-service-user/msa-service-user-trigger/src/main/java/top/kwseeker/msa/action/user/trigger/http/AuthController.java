package top.kwseeker.msa.action.user.trigger.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.kwseeker.msa.action.user.api.local.ITokenAPI;
import top.kwseeker.msa.action.user.api.model.TokenVerifiedDTO;
import top.kwseeker.msa.action.user.domain.auth.model.entity.LoginRespEntity;
import top.kwseeker.msa.action.framework.common.model.Response;
import top.kwseeker.msa.action.user.domain.auth.model.vo.LoginReqVO;
import top.kwseeker.msa.action.user.domain.auth.service.IAuthService;
import top.kwseeker.msa.action.user.trigger.http.model.dto.LoginDTO;
import top.kwseeker.msa.action.user.trigger.http.model.dto.converter.Converter;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private IAuthService authService;
    @Resource
    private ITokenAPI tokenAPI;

    @PermitAll
    @PostMapping("/login")
    public Response<LoginRespEntity> login(@RequestBody LoginDTO loginDTO) {
        LoginReqVO loginReqVO = Converter.INSTANCE.convert(loginDTO);
        LoginRespEntity loginResp = authService.login(loginReqVO);
        return Response.success(loginResp);
    }

    @PermitAll
    @PostMapping("/token/verify")
    public Response<TokenVerifiedDTO> verifyToken(@RequestParam("token") String token) {
        TokenVerifiedDTO tokenVerifiedDTO = tokenAPI.verifyToken(token);
        return Response.success(tokenVerifiedDTO);
    }
}
