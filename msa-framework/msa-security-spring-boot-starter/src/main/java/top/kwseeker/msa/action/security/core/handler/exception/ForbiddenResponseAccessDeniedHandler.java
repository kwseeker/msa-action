package top.kwseeker.msa.action.security.core.handler.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import top.kwseeker.msa.action.framework.common.exception.GlobalErrorCodes;
import top.kwseeker.msa.action.framework.common.model.Response;
import top.kwseeker.msa.action.framework.common.util.ServletUtil;
import top.kwseeker.msa.action.security.core.util.SecurityFrameworkUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  AccessDeniedException 处理器，
 *  AccessDeniedException 是 FilterSecurityInterceptor 校验失败后发出的，此异常发生后在 ExceptionTranslationFilter 中被抓获
 *  默认提供有 AccessDeniedHandlerImpl
 */
@Slf4j
public class ForbiddenResponseAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e)
            throws IOException, ServletException {
        // 打印 warn 的原因是，不定期合并 warn，看看有没恶意破坏
        log.warn("[commence][访问 URL({}) 时，用户({}) 权限不够]", request.getRequestURI(),
                SecurityFrameworkUtil.getLoginUserId(), e);
        ServletUtil.writeJSON(response, Response.fail(GlobalErrorCodes.FORBIDDEN));
    }

}