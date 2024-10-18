package top.kwseeker.msa.action.webha.app.config.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 替换掉 sentinel-spring-webmvc-adapter 中默认的 DefaultBlockExceptionHandler
 * 只能用于 SentinelWebInterceptor，对 @SentinelResource 注解的接口无效
 */
@Slf4j
@Component
public class DefaultBlockExceptionHandler implements BlockExceptionHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
        log.warn("Flow Control: request url:{}, method:{} been limited", request.getRequestURL(), request.getMethod(), e);
        // 继续抛出异常后面由 GlobalExceptionHandler 处理
        throw e;
    }
}
