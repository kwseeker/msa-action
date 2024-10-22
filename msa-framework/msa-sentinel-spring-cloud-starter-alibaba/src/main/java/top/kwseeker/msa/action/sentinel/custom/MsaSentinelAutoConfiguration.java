package top.kwseeker.msa.action.sentinel.custom;

import com.alibaba.cloud.sentinel.custom.SentinelAutoConfiguration;
import com.alibaba.cloud.sentinel.datasource.converter.JsonConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import top.kwseeker.msa.action.sentinel.adapter.spring.webmvc.MsaSentinelWebInterceptor;
import top.kwseeker.msa.action.sentinel.slots.block.user.UserAuthorityRule;
import top.kwseeker.msa.action.sentinel.web.MsaSentinelWebMvcConfigurer;

@AutoConfiguration(before = SentinelAutoConfiguration.class)
@ConditionalOnClass(ObjectMapper.class)
@ConditionalOnProperty(name = "spring.cloud.sentinel.enabled", matchIfMissing = true)
public class MsaSentinelAutoConfiguration {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean("sentinel-json-user-authority-converter")
    public JsonConverter jsonUserAuthorityConverter() {
        return new JsonConverter(objectMapper, UserAuthorityRule.class);
    }

    @Bean
    @ConditionalOnProperty(name = "spring.cloud.sentinel.filter.enabled", matchIfMissing = true)
    public MsaSentinelWebInterceptor msaSentinelWebInterceptor() {
        return new MsaSentinelWebInterceptor();
    }

    @Bean
    @ConditionalOnProperty(name = "spring.cloud.sentinel.filter.enabled", matchIfMissing = true)
    public MsaSentinelWebMvcConfigurer msaSentinelWebMvcConfigurer() {
        return new MsaSentinelWebMvcConfigurer();
    }
}
