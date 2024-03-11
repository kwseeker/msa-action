package top.kwseeker.msa.action.user.domain.auth.service;

import org.springframework.web.bind.annotation.RequestBody;
import top.kwseeker.msa.action.user.domain.auth.model.entity.LoginRespEntity;
import top.kwseeker.msa.action.user.trigger.http.dto.LoginDTO;

public interface IAuthService {

    LoginRespEntity login(@RequestBody LoginDTO loginDTO);
}
