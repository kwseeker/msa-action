package top.kwseeker.msa.action.user.api.local;

import top.kwseeker.msa.action.user.api.model.TokenVerifiedDTO;

public interface ITokenAPI {

    TokenVerifiedDTO verifyToken(String token);
}
