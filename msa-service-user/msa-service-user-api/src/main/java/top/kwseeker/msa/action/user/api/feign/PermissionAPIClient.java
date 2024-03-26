package top.kwseeker.msa.action.user.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import top.kwseeker.msa.action.framework.common.model.Response;
import top.kwseeker.msa.action.user.api.model.PermissionVerifyDTO;

@FeignClient(value = "msa-service-user", contextId = "permissionAPI", path = "/user/permission")
public interface PermissionAPIClient {

    @PostMapping("/verify")
    Response<Boolean> verify(PermissionVerifyDTO permissionVerifyDTO);
}