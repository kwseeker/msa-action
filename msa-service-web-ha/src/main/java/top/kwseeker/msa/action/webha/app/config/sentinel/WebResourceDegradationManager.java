package top.kwseeker.msa.action.webha.app.config.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class WebResourceDegradationManager {

    private final Map<WebResourceKey, BlockExceptionHandler> blockHandlerMap;

    public WebResourceDegradationManager() {
        this.blockHandlerMap = new HashMap<>();
        // 注册方式可以优化
        blockHandlerMap.put(new WebResourceKey("GET", "/flow"), (request, response, e) -> {
            log.warn("Degrade: request url:{}, method:{}, with block exception: {}", request.getRequestURL(), request.getMethod(), e.getMessage());
            // 继续抛出异常后面由 GlobalExceptionHandler 处理
            throw e;
        });
    }

    public BlockExceptionHandler getBlockHandler(HttpServletRequest request) {
        return blockHandlerMap.get(webResourceKeyFromRequest(request));
    }

    private WebResourceKey webResourceKeyFromRequest(HttpServletRequest request) {
        return new WebResourceKey(request.getMethod(), request.getRequestURI());
    }

    @Data
    @AllArgsConstructor
    static class WebResourceKey {
        String method;
        String uri;
    }
}
