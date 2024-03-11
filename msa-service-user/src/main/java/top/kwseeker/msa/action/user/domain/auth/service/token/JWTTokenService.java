package top.kwseeker.msa.action.user.domain.auth.service.token;

import org.springframework.stereotype.Service;
import top.kwseeker.msa.action.user.domain.user.model.entity.UserEntity;
import top.kwseeker.msa.action.user.types.utils.JWTUtil;

import java.util.HashMap;
import java.util.Map;

@Service
public class JWTTokenService implements ITokenService {

    private static final int expireSeconds = 7200;

    @Override
    public String createToken(UserEntity userEntity) {
        Map<String, Object> tokenContent = new HashMap<>();
        //TODO
        tokenContent.put("userId", userEntity.getId());
        tokenContent.put("username", userEntity.getUsername());
        tokenContent.put("deptId", userEntity.getDeptId());
        return JWTUtil.createJWT(tokenContent, expireSeconds);
    }
}
