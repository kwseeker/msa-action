package top.kwseeker.msa.action.user.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.kwseeker.msa.action.framework.common.model.Response;
import top.kwseeker.msa.action.user.api.model.TokenVerifiedDTO;

@FeignClient(value = "msa-service-user", contextId = "tokenAPI", path = "/user/auth")
//@ConditionalOnMissingBean(ITokenAPI.class)
public interface TokenAPIClient {

    @PostMapping("/token/verify")
    Response<TokenVerifiedDTO> verifyToken(@RequestParam("token") String token);
}
