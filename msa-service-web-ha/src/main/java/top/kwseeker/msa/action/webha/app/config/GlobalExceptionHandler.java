package top.kwseeker.msa.action.webha.app.config;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import top.kwseeker.msa.action.framework.common.model.Response;

import static top.kwseeker.msa.action.framework.common.exception.GlobalErrorCodes.*;

@Slf4j
@Component
@ControllerAdvice(basePackages = "top.kwseeker.msa.action.webha")
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = BlockException.class)
    public Response<String> blockExceptionHandler(BlockException blockException) {
        return Response.fail(REQUEST_DEGRADE_ERROR);
    }

    /**
     * 兜底异常处理
     */
    @ResponseBody
    @ExceptionHandler(value = Throwable.class)
    public Response<String> throwableHandler(Throwable throwable) {
        log.error("throwableHandler: ", throwable);
        return Response.fail(INTERNAL_SERVER_ERROR);
    }
}
