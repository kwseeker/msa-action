package top.kwseeker.msa.action.webha.app.config.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 替换掉 sentinel-spring-webmvc-adapter 中默认的 DefaultBlockExceptionHandler
 * 只能用于 SentinelWebInterceptor，对 @SentinelResource 注解的接口无效
 */
@Slf4j
@Component
public class DefaultBlockExceptionHandler implements BlockExceptionHandler {

    @Resource
    private WebResourceDegradationManager webResourceDegradationManager;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
        // 先判断请求对应的降级处理器是否存在
        BlockExceptionHandler blockHandler = webResourceDegradationManager.getBlockHandler(request);
        if (blockHandler != null) {
            blockHandler.handle(request, response, e);
            return;
        }

        log.warn("Degrade: request url:{}, method:{}, with block exception: {}", request.getRequestURL(), request.getMethod(), e.getMessage());
        // 继续抛出异常后面由 GlobalExceptionHandler 处理
        throw e;
    }
}
