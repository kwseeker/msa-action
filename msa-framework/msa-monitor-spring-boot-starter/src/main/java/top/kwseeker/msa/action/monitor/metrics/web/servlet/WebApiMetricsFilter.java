package top.kwseeker.msa.action.monitor.metrics.web.servlet;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.util.Map;
import java.util.Set;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.MergedAnnotationCollectors;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import top.kwseeker.msa.action.monitor.annotation.MetricsWebApi;

/**
 * Msa请求监控指标的Servlet过滤器
 */
public class WebApiMetricsFilter extends OncePerRequestFilter {

    private static final Map<AnnotatedElement, MetricsWebApi> cache = new ConcurrentReferenceHashMap<>();
    private final WebApiMetrics webApiMetrics;

    public WebApiMetricsFilter(WebApiMetrics webApiMetrics) {
        this.webApiMetrics = webApiMetrics;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 只拦截带 MetricsWebApi 注解的接口请求
        Object handler = getHandler(request);
        MetricsWebApi metricsWebApiAnnotation = getWebApiAnnotation(handler);
        if (metricsWebApiAnnotation == null) {
            filterChain.doFilter(request, response);
            return;
        }

        TimingContext timingContext = TimingContext.start();    //有时其他业务可能也会记录请求时间，有的话可以复用
        try {
            filterChain.doFilter(request, response);
            if (!request.isAsyncStarted()) {    //异步请求不记录
                this.record(timingContext, request, response);
            }
        } catch (Exception var6) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            this.record(timingContext, request, response);
            throw var6;
        }
    }

    /**
     * 统计数据记录
     */
    private void record(TimingContext timingContext, HttpServletRequest request, HttpServletResponse response) {
        webApiMetrics.record(timingContext, request, response);
    }

    private Object getHandler(HttpServletRequest request) {
        return request.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);
    }

    private MetricsWebApi getWebApiAnnotation(Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod)handler;
            // 先获取方法上的注解
            MetricsWebApi webApiAnnotation = findWebApiAnnotation(handlerMethod.getMethod());
            if (webApiAnnotation != null) {
                return webApiAnnotation;
            }
            // 方法上没有注解的话，再获取类上的注解
            webApiAnnotation = findWebApiAnnotation(handlerMethod.getBeanType());
            if (webApiAnnotation != null) {
                return webApiAnnotation;
            }
            return null;
        }
        // 暂时不支持其他的handler类型, 需要的的话可以再添加
        return null;
    }

    private MetricsWebApi findWebApiAnnotation(AnnotatedElement element) {
        MetricsWebApi result = cache.get(element);
        if(result != null) {
            return result;
        }
        MergedAnnotations annotations = MergedAnnotations.from(element);
        if (annotations.isPresent(MetricsWebApi.class)) {
            Set<MetricsWebApi> annotationsOnMethod = annotations.stream(MetricsWebApi.class)
                    .collect(MergedAnnotationCollectors.toAnnotationSet());
            result = annotationsOnMethod.iterator().next();
            cache.put(element, result);
            return result;
        }
        return null;
    }
}
