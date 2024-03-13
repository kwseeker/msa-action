package top.kwseeker.msa.action.user.domain.auth.service;

import top.kwseeker.msa.action.user.domain.auth.model.entity.LoginRespEntity;
import top.kwseeker.msa.action.user.domain.auth.model.vo.LoginReqVO;

public interface IAuthService {

    LoginRespEntity login(LoginReqVO loginDTO);
}
