package top.kwseeker.msa.action.monitor.endpoint;

import io.prometheus.metrics.expositionformats.ExpositionFormatWriter;
import io.prometheus.metrics.expositionformats.PrometheusTextFormatWriter;
import io.prometheus.metrics.model.registry.PrometheusRegistry;
import io.prometheus.metrics.model.snapshots.MetricSnapshots;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.boot.actuate.metrics.export.prometheus.TextOutputFormat;
import org.springframework.lang.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;

/**
 * MSA监控端点: /actuator/msa-monitor
 */
@WebEndpoint(id = "msa-monitor")
public class MsaMonitorEndpoint {

    private final PrometheusRegistry collectorRegistry;
    // 缓存上次返回的 scrape 页面大小，可以减少下次 WriteBuffer 自动扩容的开销
    private volatile int nextMetricsScrapeSize = 16;

    public MsaMonitorEndpoint(PrometheusRegistry collectorRegistry) {
        this.collectorRegistry = collectorRegistry;
    }

    /**
     * 可以像这样请求
     * curl <a href="http://localhost:8080/actuator/msa-monitor?includedNames=metricName1,metricName2"/>
     */
    @ReadOperation(
            producesFrom = TextOutputFormat.class   //输出格式
    )
    public WebEndpointResponse<String> scrape(TextOutputFormat format, @Nullable Set<String> includedNames) {
        //从 PrometheusRegistry 中读取 includedNames 指定的指标数据
        MetricSnapshots metricSnapshots = this.collectorRegistry.scrape(
                t -> includedNames == null || includedNames.contains(t));
        //将 MetricsSnapshots 转换为 Prometheus 的文本格式并通过 WebEndpointResponse 返回
        try {
            ExpositionFormatWriter writer = new PrometheusTextFormatWriter(false);
            // 当要写入数据大于初始分配的缓冲大小时会自动扩容，不需要担心缓冲大小不足
            ByteArrayOutputStream baos = new ByteArrayOutputStream(this.nextMetricsScrapeSize);
            writer.write(baos, metricSnapshots);
            String scrapePage = baos.toString();
            this.nextMetricsScrapeSize = scrapePage.length() + 1024;
            return new WebEndpointResponse<>(scrapePage, format);
        } catch (IOException e) {
            throw new IllegalStateException("Writing metrics failed", e);
        }
    }
}
