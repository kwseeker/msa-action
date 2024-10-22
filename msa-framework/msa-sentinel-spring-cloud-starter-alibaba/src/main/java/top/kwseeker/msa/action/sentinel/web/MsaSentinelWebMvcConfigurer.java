package top.kwseeker.msa.action.sentinel.web;

import com.alibaba.cloud.sentinel.SentinelProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.kwseeker.msa.action.sentinel.adapter.spring.webmvc.MsaSentinelWebInterceptor;

import java.util.Optional;

public class MsaSentinelWebMvcConfigurer implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(MsaSentinelWebMvcConfigurer.class);

    @Autowired
    private SentinelProperties sentinelProperties;
    @Autowired
    private Optional<MsaSentinelWebInterceptor> msaSentinelWebInterceptorOptional;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (!msaSentinelWebInterceptorOptional.isPresent()) {
            return;
        }
        SentinelProperties.Filter filterConfig = sentinelProperties.getFilter();
        registry.addInterceptor(msaSentinelWebInterceptorOptional.get())
                .order(filterConfig.getOrder() - 1) // 需要在 SentinelWebInterceptor 之前执行
                .addPathPatterns(filterConfig.getUrlPatterns());
        log.info("[Sentinel Starter] register MsaSentinelWebInterceptor with urlPatterns: {}.",
                filterConfig.getUrlPatterns());
    }
}
