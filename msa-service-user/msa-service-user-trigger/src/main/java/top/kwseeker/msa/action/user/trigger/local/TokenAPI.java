package top.kwseeker.msa.action.user.trigger.local;

import org.springframework.stereotype.Service;
import top.kwseeker.msa.action.user.api.local.ITokenAPI;
import top.kwseeker.msa.action.user.api.model.TokenVerifiedDTO;
import top.kwseeker.msa.action.user.domain.auth.model.vo.TokenVerifiedVO;
import top.kwseeker.msa.action.user.domain.auth.service.token.ITokenService;

import javax.annotation.Resource;
import java.util.Collections;

@Service
public class TokenAPI implements ITokenAPI {

    @Resource
    private ITokenService tokenService;

    @Override
    public TokenVerifiedDTO verifyToken(String token) {
        TokenVerifiedVO tokenVerifiedVO = tokenService.verifyToken(token);
        return TokenVerifiedDTO.builder()
                .userId(tokenVerifiedVO.getUserId())
                .username(tokenVerifiedVO.getUsername())
                .scopes(Collections.emptyList())    //TODO
                .build();
    }
}
