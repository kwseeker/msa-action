package top.kwseeker.msa.action.user.trigger.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kwseeker.msa.action.framework.common.model.Response;
import top.kwseeker.msa.action.user.api.model.PermissionVerifyDTO;
import top.kwseeker.msa.action.user.trigger.local.PermissionAPI;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;

@Slf4j
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Resource
    private PermissionAPI permissionAPI;

    @PermitAll
    @PostMapping("/verify")
    public Response<Boolean> verify(PermissionVerifyDTO permissionVerifyDTO) {
        boolean result = permissionAPI.verify(permissionVerifyDTO);
        return Response.success(result);
    }
}