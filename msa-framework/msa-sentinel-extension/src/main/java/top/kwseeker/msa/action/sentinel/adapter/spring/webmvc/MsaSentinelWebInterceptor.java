package top.kwseeker.msa.action.sentinel.adapter.spring.webmvc;

import cn.hutool.json.JSONUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import top.kwseeker.msa.action.framework.common.context.UserContext;
import top.kwseeker.msa.action.sentinel.context.UserContextUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 从请求中取出用户信息，设置到ThreadLocal中，用于后面 UserAuthoritySlot 读取
 */
public class MsaSentinelWebInterceptor implements HandlerInterceptor {

    private static final String LOGIN_USER_HEADER = "Login-User";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String loginUserHeader = request.getHeader(LOGIN_USER_HEADER);
        if (StringUtil.isNotBlank(loginUserHeader)) {
            UserContext userContext = JSONUtil.toBean(loginUserHeader, UserContext.class);
            if (userContext != null) {
                UserContextUtil.enter(userContext);
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContextUtil.exit();
    }
}
