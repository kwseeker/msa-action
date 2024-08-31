package top.kwseeker.msa.web.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.kwseeker.msa.action.framework.common.exception.GlobalErrorCodes;
import top.kwseeker.msa.action.framework.common.model.Response;

import javax.servlet.http.HttpServletRequest;

/**
 * 统一异常处理
 * <a href="https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-advice.html#/">Controller Advice</a>
 * ControllerAdvice 是实现统一异常处理的一种方式，也不仅仅可以做统一异常处理
 * 还可以借助：HandlerExceptionResolver 实现，参考 DispatcherServlet 源码：
 *  private List<HandlerExceptionResolver> handlerExceptionResolvers;
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {



    /**
     * 兜底处理
     */
    @ExceptionHandler(value = {Throwable.class})
    public Response<?> defaultExceptionHandler(HttpServletRequest req, Throwable ex) {
        //记录日志
        String requestURI = req.getRequestURI();
        log.error("统一异常处理，兜底处理，requestURI={}，ex:", requestURI, ex);
        return Response.fail(GlobalErrorCodes.INTERNAL_SERVER_ERROR);
    }
}
