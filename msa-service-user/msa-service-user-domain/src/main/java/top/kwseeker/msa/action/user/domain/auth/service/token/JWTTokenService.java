package top.kwseeker.msa.action.user.domain.auth.service.token;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;
import top.kwseeker.msa.action.user.domain.auth.model.vo.TokenVerifiedVO;
import top.kwseeker.msa.action.user.domain.user.model.entity.UserEntity;
import top.kwseeker.msa.action.user.types.utils.JWTUtil;

import java.util.HashMap;
import java.util.Map;

@Service
public class JWTTokenService implements ITokenService {

    private static final int expireSeconds = 12*3600;

    @Override
    public String createToken(UserEntity userEntity) {
        Map<String, Object> tokenContent = new HashMap<>();
        //TODO
        tokenContent.put("userId", userEntity.getId());
        tokenContent.put("username", userEntity.getUsername());
        tokenContent.put("deptId", userEntity.getDeptId());
        return JWTUtil.createJWT(tokenContent, expireSeconds);
    }

    @Override
    public TokenVerifiedVO verifyToken(String token) {
        DecodedJWT decodedJWT = JWTUtil.verifyJWT(token);
        return TokenVerifiedVO.builder()
                .userId(decodedJWT.getClaims().get("userId").asLong())
                .username(decodedJWT.getClaims().get("username").asString())
                //.userId(decodedJWT.getClaims().get("deptId").asLong())
                .build();
    }
}
