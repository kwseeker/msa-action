package top.kwseeker.msa.config.casbin;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 这里使用spring.datasource, 但是Casbin暂时不支持直接通过Spring数据源
 */
@Data
@ConfigurationProperties(prefix = "spring.datasource", ignoreInvalidFields = true)
public class CasbinDataSourceProperties {

    private String driverClassName;
    private String url;
    private String username;
    private String password;
}
