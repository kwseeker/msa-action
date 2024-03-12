package top.kwseeker.msa.action.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import top.kwseeker.msa.action.framework.common.core.TokenType;

import java.util.Collections;
import java.util.List;

/**
 * 自定义安全配置
 */
@Data
@ConfigurationProperties(prefix = "msa.security")
public class MsaSecurityProperties {

    private TokenType tokenType = TokenType.JWT;

    private String tokenHeader = "Authorization";

    /**
     * 免登录的 URL 列表，在方法上添加 @PermitAll 注解
     */
    private List<String> permitAllUrls = Collections.emptyList();

    /**
     * PasswordEncoder
     */
    private Integer passwordEncoderLength = 4;
}
