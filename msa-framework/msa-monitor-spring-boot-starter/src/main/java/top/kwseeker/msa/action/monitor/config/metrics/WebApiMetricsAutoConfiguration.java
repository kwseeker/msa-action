package top.kwseeker.msa.action.monitor.config.metrics;

import io.prometheus.metrics.model.registry.PrometheusRegistry;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import top.kwseeker.msa.action.monitor.config.MsaMonitorAutoConfiguration;
import top.kwseeker.msa.action.monitor.metrics.web.servlet.WebApiMetrics;
import top.kwseeker.msa.action.monitor.metrics.web.servlet.WebApiMetricsFilter;

/**
 * 针对请求监控指标的自动配置
 */
@AutoConfiguration(
        after = MsaMonitorAutoConfiguration.class
)
@ConditionalOnWebApplication
public class WebApiMetricsAutoConfiguration {

    @Bean
    public WebApiMetrics webApiMetrics(PrometheusRegistry prometheusRegistry) {
        return new WebApiMetrics(prometheusRegistry);
    }

    /**
     * Web Servlet 拦截器
     * 用于拦截被注解的请求，向采集器中记录数据，拦截器首次拦截某个请求会创建并注册采集器
     */
    @Bean
    @ConditionalOnWebApplication
    public FilterRegistrationBean<WebApiMetricsFilter> msaRequestMetricsFilter(WebApiMetrics webApiMetrics) {
        return new FilterRegistrationBean<>(new WebApiMetricsFilter(webApiMetrics));
    }
}
