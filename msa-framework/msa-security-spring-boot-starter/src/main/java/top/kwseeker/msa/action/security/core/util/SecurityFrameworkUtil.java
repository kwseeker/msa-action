package top.kwseeker.msa.action.security.core.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.util.StringUtils;
import top.kwseeker.msa.action.security.core.authentication.MsaWebAuthenticationDetails;
import top.kwseeker.msa.action.security.core.LoginUser;
import top.kwseeker.msa.action.security.core.RequestInfo;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

/**
 * 从请求获取Token用于校验
 * 校验通过后创建认证凭据 Authentication 对象（包含用户信息）保存到安全上下文
 * 后续权限检查时从安全上下文内读取 Authentication 对象获取用户信息
 */
public class SecurityFrameworkUtil {

    public static final String AUTHORIZATION_BEARER = "Bearer";

    public static final String LOGIN_USER_HEADER = "Login-User";

    private SecurityFrameworkUtil() {}


    /**
     * 从请求头 "Bearer" 中获得认证 Token
     *
     * @param request 请求
     * @param header 认证 Token 对应的 Header 名字
     * @return 认证 Token
     */
    public static String obtainAuthorization(HttpServletRequest request, String header) {
        String authorization = request.getHeader(header);
        if (!StringUtils.hasText(authorization)) {
            return null;
        }
        int index = authorization.indexOf(AUTHORIZATION_BEARER + " ");
        if (index == -1) { // 未找到
            return null;
        }
        return authorization.substring(index + 7).trim();
    }


    /**
     * Token校验通过后创建用户凭证信息
     */
    private static Authentication buildAuthentication(LoginUser loginUser, HttpServletRequest request) {
        // 创建 UsernamePasswordAuthenticationToken 对象, TODO 这个TOKEN名字太迷惑了重新封装一个
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginUser, null, Collections.emptyList());
        //TODO
        authenticationToken.setDetails(new MsaWebAuthenticationDetails(request));
        return authenticationToken;
    }


    /**
     * 获得当前认证信息，认证通过后会发布一个认证通过的凭据 Authentication，内部认证信息，保存到安全上下文中
     *
     * @return 认证信息
     */
    public static Authentication getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return null;
        }
        return context.getAuthentication();
    }

    /**
     * 从认证信息中获取当前用户信息
     *
     * @return 当前用户
     */
    @Nullable
    public static LoginUser getLoginUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        return authentication.getPrincipal() instanceof LoginUser ? (LoginUser) authentication.getPrincipal() : null;
    }

    @Nullable
    public static RequestInfo getRequestInfo() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        LoginUser loginUser = authentication.getPrincipal() instanceof LoginUser ?
                (LoginUser) authentication.getPrincipal() : null;
        MsaWebAuthenticationDetails details = authentication.getDetails() instanceof MsaWebAuthenticationDetails ?
                (MsaWebAuthenticationDetails) authentication.getDetails() : null;
        return RequestInfo.builder()
                .loginUser(loginUser)
                .requestDetails(details)
                .build();
    }

    /**
     * 从认证信息中获取当前用户的编号
     *
     * @return 用户编号
     */
    @Nullable
    public static Long getLoginUserId() {
        LoginUser loginUser = getLoginUser();
        return loginUser != null ? loginUser.getId() : null;
    }

    /**
     * 设置当前用户
     *
     * @param loginUser 登录用户
     * @param request   请求
     */
    public static void setLoginUser(LoginUser loginUser, HttpServletRequest request) {
        // 创建 Authentication，并设置到上下文
        Authentication authentication = buildAuthentication(loginUser, request);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 额外设置到 request 中，用于 ApiAccessLogFilter 可以获取到用户编号；
        // 原因是，Spring Security 的 Filter 在 ApiAccessLogFilter 后面，在它记录访问日志时，线上上下文已经没有用户编号等信息
        //WebFrameworkUtils.setLoginUserId(request, loginUser.getId()); //TODO
        //WebFrameworkUtils.setLoginUserType(request, loginUser.getUserType());
    }
}