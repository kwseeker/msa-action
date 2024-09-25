package top.kwseeker.msa.action.monitor.metrics.web.servlet;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Msa请求监控指标的Servlet过滤器
 */
public class WebApiMetricsFilter extends OncePerRequestFilter {

    private final WebApiMetrics webApiMetrics;

    public WebApiMetricsFilter(WebApiMetrics webApiMetrics) {
        this.webApiMetrics = webApiMetrics;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //1 获取 Handler 方法注解

        try {
            filterChain.doFilter(request, response);
            if (!request.isAsyncStarted()) {    //异步请求不记录
                Throwable exception = this.fetchException(request);
                this.record(timingContext, request, response);
            }
        } catch (Exception var6) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            this.record(timingContext, request, response, this.unwrapNestedServletException(var6));
            throw var6;
        }
    }

    /**
     * 统计数据记录
     */
    private void record(TimingContext timingContext, HttpServletRequest request, HttpServletResponse response) {

        webApiMetrics.record();
    }

    private static class TimingContext {
        private final long startTime;   // 开始时间ms
        private final TimeUnit timeUnit;

        public TimingContext(long startTime, TimeUnit timeUnit) {
            this.startTime = startTime;
            this.timeUnit = timeUnit;
        }

        long getStartTime() {
            return startTime;
        }

        TimeUnit getTimeUnit() {
            return timeUnit;
        }
    }
}
