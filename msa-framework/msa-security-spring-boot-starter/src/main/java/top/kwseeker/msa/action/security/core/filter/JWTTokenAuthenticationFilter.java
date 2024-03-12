package top.kwseeker.msa.action.security.core.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import top.kwseeker.msa.action.security.config.MsaSecurityProperties;
import top.kwseeker.msa.action.security.core.LoginUser;
import top.kwseeker.msa.action.security.core.util.SecurityFrameworkUtil;
import top.kwseeker.msa.action.user.api.ITokenAPI;
import top.kwseeker.msa.action.user.api.model.TokenVerifiedDTO;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT Token校验
 */
@Slf4j
public class JWTTokenAuthenticationFilter extends OncePerRequestFilter implements TokenAuthenticationFilter {

    private final MsaSecurityProperties msaSecurityProperties;
    private final ITokenAPI tokenAPI;

    public JWTTokenAuthenticationFilter(MsaSecurityProperties msaSecurityProperties, ITokenAPI tokenAPI) {
        this.msaSecurityProperties = msaSecurityProperties;
        this.tokenAPI = tokenAPI;
    }

    /**
     * 解析请求头 Login-User Authorization,
     * Login-User 存在说明前面的微服务已经做过Token校验了，这里直接解析下放到请求上下文即可；
     * Login-User 不存在 Authorization 存在，说明需要对Token校验
     * 都不存在，可能是不需要认证的请求
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 微服务中，可能当前服务是后台服务，请求都来自网关或其他微服务，而且前面的服务可能已经验证过token了，
        // 这时直接通过自定义请求头“login-user”将用户信息透传过来，不再需要校验Token
        LoginUser loginUser = buildLoginUserByHeader(request);

        // 说明Token还没有校验
        // 那就校验 Token, 读取登录时颁发Token时存储的对应的用户信息
        if (loginUser == null) {
            String token = SecurityFrameworkUtil.obtainAuthorization(request, msaSecurityProperties.getTokenHeader());
            if (StrUtil.isNotBlank(token)) {
                //Integer loginUserType = WebFrameworkUtils.getLoginUserType(request);
                loginUser = buildLoginUserByToken(token);
            }
        }

        // 再往request attributes 中存一下方便出了安全过滤器链后仍然可以读取
        if (loginUser != null) {
            log.debug("loginUser: {}", loginUser);
            SecurityFrameworkUtil.setLoginUser(loginUser, request);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 从请求头取已经认证过Token的用户信息
     *
     * @return 已登录的用户信息
     */
    private LoginUser buildLoginUserByHeader(HttpServletRequest request) {
        String loginUserStr = request.getHeader(SecurityFrameworkUtil.LOGIN_USER_HEADER);
        return StrUtil.isNotEmpty(loginUserStr) ? JSONUtil.toBean(loginUserStr, LoginUser.class) : null;
    }

    //private LoginUser buildLoginUserByToken(String token, Integer userType) {
    private LoginUser buildLoginUserByToken(String token) {
        try {
            // 校验访问令牌
            TokenVerifiedDTO tokenVerifiedDTO = tokenAPI.verifyToken(token);

            // 用户类型不匹配，无权限
            //if (ObjectUtil.notEqual(accessToken.getUserType(), userType)) {
            //    throw new AccessDeniedException("错误的用户类型");
            //}
            // 构建登录用户
            return LoginUser.builder()
                    .id(tokenVerifiedDTO.getUserId())
                    //.userType(tokenPO.getUserType())
                    .scopes(tokenVerifiedDTO.getScopes())
                    .build();
        } catch (Exception e) {
            log.warn("verify token and get user info failed: ", e);
            // 校验 Token 不通过时，考虑到一些接口是无需登录的，所以直接返回 null 即可
            return null;
        }
    }
}
