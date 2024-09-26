package top.kwseeker.msa.action.monitor.metrics.web.servlet;

import io.prometheus.metrics.core.metrics.Counter;
import io.prometheus.metrics.model.registry.PrometheusRegistry;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Web请求监控指标集合
 * 监控指标：
 * 1. 统计接口访问次数
 *    spring boot actuator 中已经包含了此统计项，但是仅仅是记录从节点启动到当前时间段内的访问次数，
 *    这里我们统计从接口上线到当前时间段所有节点的所有访问次数。
 *    使用 Counter 统计, 仅统计成功访问次数
 * 2. 统计接口QPS
 *    这里统计一段时间内（比如10分钟，做成可配置的）接口访问的QPS。
 *    其实统计QPS在网关以及反向代理服务器中完成更合适，因为如果并发请求很大，在过滤器中统计可能无法即时响应。
 * 3. 统计接口一段时间内响应时间的百分位直方图
 *    比如统计1小时内接口响应时间的 0.5 0.9 0.99 百分位直方图。
 * 4. 统计接口响应超过设定时间（比如300ms、500ms、1000ms）的次数
 */
public class WebApiMetrics {

    private final PrometheusRegistry prometheusRegistry;
    private Counter apiRequestSuccessCounter;
    private ApiQpsCounter apiQpsCounter;
    private final ScheduledExecutorService clearTimeoutRecordData;

    public WebApiMetrics(PrometheusRegistry prometheusRegistry) {
        this.prometheusRegistry = prometheusRegistry;
        this.clearTimeoutRecordData = Executors.newSingleThreadScheduledExecutor();
        registerMetrics();
    }

    /**
     * 将指标采集器注册到注册表
     * 一个采集器可以采集一组数据，比如统计各接口成功访问次数，各个接口都有一个独立的计数器，各接口通过labels区分
     */
    public void registerMetrics() {
        // 统计各接口成功访问次数
        Counter apiRequestSuccessCounter = Counter.builder()
                .name("api_request_success_total")
                .help("Total number of requests to the API")
                .labelNames("uri", "method")    //各接口通过labels标签区分
                .build();
        // 统计各接口的QPS, 仅保存10分钟内的数据
        ApiQpsCounter apiQpsCounter = new ApiQpsCounter(Counter.builder()
                .name("api_request_qps")
                .help("QPS of requests to the API")
                .labelNames("uri", "method", "time")    //time 的值是 yyyy-MM-dd HH:mm:ss 格式
                .build(), clearTimeoutRecordData);
        // 统计各接口一段时间内响应时间的百分位直方图
        // TODO
        // 统计接口响应超过设定时间（比如300ms、500ms、1000ms）的次数
        // TODO

        this.apiRequestSuccessCounter = apiRequestSuccessCounter;
        prometheusRegistry.register(apiRequestSuccessCounter);
        this.apiQpsCounter = apiQpsCounter;
        prometheusRegistry.register(apiQpsCounter.getCounter());
    }

    /**
     * 记录统计数据
     */
    public void record(TimingContext timingContext, HttpServletRequest request, HttpServletResponse response) {
        // 接口访问次数，只统计成功的访问次数
        if (response.getStatus() == HttpStatus.OK.value()) {
            apiRequestSuccessCounter.labelValues(request.getRequestURI(), request.getMethod()).inc();
        }
        // 接口QPS
        long startTimeMillis = timingContext.getStartTimeInMillis();
        apiQpsCounter.labelValues(request.getRequestURI(), request.getMethod(), startTimeMillis)
                .inc();
    }
}
