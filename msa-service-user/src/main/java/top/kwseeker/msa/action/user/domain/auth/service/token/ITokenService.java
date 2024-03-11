package top.kwseeker.msa.action.user.domain.auth.service.token;

import top.kwseeker.msa.action.user.domain.user.model.entity.UserEntity;

public interface ITokenService {

    String createToken(UserEntity userEntity);
}
