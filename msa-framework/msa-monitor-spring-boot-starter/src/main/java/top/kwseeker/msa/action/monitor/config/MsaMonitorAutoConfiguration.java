package top.kwseeker.msa.action.monitor.config;

import io.prometheus.metrics.model.registry.PrometheusRegistry;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.kwseeker.msa.action.monitor.endpoint.MsaMonitorEndpoint;

/**
 * 总的配置类
 * 实现：
 * 1. 自定义监控配置加载（开关、监控指标配置列表）
 * 2. 数据采集器注册表Bean
 * 3. 监控指标注册
 * 4. 指标数据提取端点创建
 */
@AutoConfiguration
@ConditionalOnClass(PrometheusRegistry.class)
@ConditionalOnProperty(name = "msa.monitor.enable", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(MsaMonitorProperties.class) //启用属性绑定，将配置文件中相应的内容绑定到MsaMonitorProperties Bean实例中
public class MsaMonitorAutoConfiguration {

    /**
     * Prometheus client_java 中采集器的注册表是单例对象
     */
    @Bean
    public PrometheusRegistry prometheusRegistry() {
        return PrometheusRegistry.defaultRegistry;
    }

    @Configuration(proxyBeanMethods = false)
    public static class MsaMonitorEndpointConfiguration {

        @Bean
        public MsaMonitorEndpoint msaMonitorEndpoint(PrometheusRegistry registry) {
            return new MsaMonitorEndpoint(registry);
        }
    }
}
