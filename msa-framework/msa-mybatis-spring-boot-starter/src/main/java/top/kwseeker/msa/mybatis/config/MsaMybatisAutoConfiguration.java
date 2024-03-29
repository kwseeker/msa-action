package top.kwseeker.msa.mybatis.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import top.kwseeker.msa.mybatis.core.handler.field.CommonFieldsFillHandler;

@AutoConfiguration
@EnableTransactionManagement(proxyTargetClass = true)
public class MsaMybatisAutoConfiguration {

    @Bean
    public CommonFieldsFillHandler commonFieldsFillHandler() {
        return new CommonFieldsFillHandler();
    }
}
