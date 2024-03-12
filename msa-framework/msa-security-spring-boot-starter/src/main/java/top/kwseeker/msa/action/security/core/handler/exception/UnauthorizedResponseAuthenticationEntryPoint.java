package top.kwseeker.msa.action.security.core.handler.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import top.kwseeker.msa.action.framework.common.exception.GlobalErrorCodes;
import top.kwseeker.msa.action.framework.common.model.Response;
import top.kwseeker.msa.action.framework.common.util.ServletUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * SpringSecurityException异常处理器，
 * 此异常表示未通过身份验证，因此服务器会发回一个响应，指示必须进行身份验证，或重定向到特定的web页面
 * 当发生异常后在 ExceptionTranslationFilter 中被调用
 */
@Slf4j
public class UnauthorizedResponseAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
        log.debug("[commence][访问 URL({}) 时，没有登录]", request.getRequestURI(), e);
        ServletUtil.writeJSON(response, Response.fail(GlobalErrorCodes.UNAUTHORIZED));
    }
}