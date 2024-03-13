package top.kwseeker.msa.action.user.domain.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.kwseeker.msa.action.user.domain.auth.model.entity.LoginRespEntity;
import top.kwseeker.msa.action.user.domain.auth.model.vo.LoginReqVO;
import top.kwseeker.msa.action.user.domain.auth.service.token.ITokenService;
import top.kwseeker.msa.action.user.domain.user.model.entity.UserEntity;
import top.kwseeker.msa.action.user.domain.user.service.IUserService;
import top.kwseeker.msa.action.user.types.common.UserErrorCodes;
import top.kwseeker.msa.action.user.types.exception.UserDomainException;

import javax.annotation.Resource;

@Slf4j
@Service
public class AuthService implements IAuthService {

    @Resource
    private IUserService userService;
    @Resource
    private ITokenService tokenService;

    @Override
    public LoginRespEntity login(LoginReqVO loginReqVO) {
        String username = loginReqVO.getUsername();
        //先忽略密码摘要加密
        String password = loginReqVO.getPassword();

        //身份认证
        UserEntity userEntity = userService.getUserByUsername(username);
        if (userEntity == null || !password.equals(userEntity.getPassword())) {
            throw new UserDomainException(UserErrorCodes.INVALID_USERNAME_OR_PASSWORD);
        }

        //发放令牌
        String token = tokenService.createToken(userEntity);
        log.info("login token: {}", token);

        //记录登录信息 TODO

        return LoginRespEntity.builder()
                .userId(userEntity.getId())
                .accessToken(token)
                .build();
    }
}
