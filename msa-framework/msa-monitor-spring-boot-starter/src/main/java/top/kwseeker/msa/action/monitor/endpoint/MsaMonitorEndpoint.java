package top.kwseeker.msa.action.monitor.endpoint;

import io.prometheus.metrics.model.registry.PrometheusRegistry;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.boot.actuate.metrics.export.prometheus.TextOutputFormat;
import org.springframework.lang.Nullable;

import java.util.Set;

/**
 * MSA监控端点: /actuator/msa-monitor
 */
@WebEndpoint(id = "msa-monitor")
public class MsaMonitorEndpoint {

    private final PrometheusRegistry collectorRegistry;

    public MsaMonitorEndpoint(PrometheusRegistry collectorRegistry) {
        this.collectorRegistry = collectorRegistry;
    }

    @ReadOperation(
            producesFrom = TextOutputFormat.class
    )
    public WebEndpointResponse<String> scrape(TextOutputFormat format, @Nullable Set<String> includedNames) {
        //TODO
        String scrapePage = "";
        return new WebEndpointResponse<>(scrapePage, format);
    }
}
