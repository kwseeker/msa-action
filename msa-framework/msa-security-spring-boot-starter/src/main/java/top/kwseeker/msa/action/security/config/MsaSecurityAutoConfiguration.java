package top.kwseeker.msa.action.security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import top.kwseeker.msa.action.security.core.permission.IPermissionVerifier;
import top.kwseeker.msa.action.security.core.permission.DefaultPermissionVerifier;
import top.kwseeker.msa.action.user.api.IPermissionAPI;

@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(MsaSecurityProperties.class)
public class MsaSecurityAutoConfiguration {

    //自定义权限检查，由 @PreAuthorize() 注入到过滤器流程，在 FilterSecurityInterceptor 中调用
    @Bean("pv")
    public IPermissionVerifier defaultPermissionVerifier(IPermissionAPI permissionService) {
        return new DefaultPermissionVerifier(permissionService);
    }
}
