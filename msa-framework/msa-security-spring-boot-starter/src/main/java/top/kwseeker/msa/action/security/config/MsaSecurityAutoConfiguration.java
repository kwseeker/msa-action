package top.kwseeker.msa.action.security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import top.kwseeker.msa.action.framework.common.core.TokenType;
import top.kwseeker.msa.action.security.core.filter.JWTTokenAuthenticationFilter;
import top.kwseeker.msa.action.security.core.filter.OAuth2TokenAuthenticationFilter;
import top.kwseeker.msa.action.security.core.filter.TokenAuthenticationFilter;
import top.kwseeker.msa.action.security.core.permission.IPermissionVerifier;
import top.kwseeker.msa.action.security.core.permission.DefaultPermissionVerifier;
import top.kwseeker.msa.action.user.api.feign.PermissionAPIClient;
import top.kwseeker.msa.action.user.api.feign.TokenAPIClient;
import top.kwseeker.msa.action.user.api.local.IPermissionAPI;
import top.kwseeker.msa.action.user.api.local.ITokenAPI;

import javax.annotation.Resource;

@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(MsaSecurityProperties.class)
public class MsaSecurityAutoConfiguration {

    @Resource
    private MsaSecurityProperties msaSecurityProperties;

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter(@Autowired(required = false) ITokenAPI tokenAPI,
                                                               @Autowired(required = false) TokenAPIClient tokenAPIClient) {
        if (TokenType.JWT == msaSecurityProperties.getTokenType()) {
            if (tokenAPI != null) {
                return new JWTTokenAuthenticationFilter(msaSecurityProperties, tokenAPI);
            } else if (tokenAPIClient != null) {
                return new JWTTokenAuthenticationFilter(msaSecurityProperties, tokenAPIClient);
            } else {
                throw new RuntimeException("No tokenAPI bean available !");
            }
        } else if (TokenType.OAuth2 == msaSecurityProperties.getTokenType()) {
            return new OAuth2TokenAuthenticationFilter();
        } else {
            throw new RuntimeException("Unknown token type: " + msaSecurityProperties.getTokenType());
        }
    }

    //自定义权限检查，由 @PreAuthorize() 注入到过滤器流程，在 FilterSecurityInterceptor 中调用
    @Bean("pv")
    public IPermissionVerifier defaultPermissionVerifier(@Autowired(required = false) IPermissionAPI permissionService,
                                                         @Autowired(required = false) PermissionAPIClient permissionAPIClient) {
        return new DefaultPermissionVerifier(permissionService, permissionAPIClient);
    }
}
