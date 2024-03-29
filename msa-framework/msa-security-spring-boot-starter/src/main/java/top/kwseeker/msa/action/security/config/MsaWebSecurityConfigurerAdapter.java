package top.kwseeker.msa.action.security.config;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import top.kwseeker.msa.action.security.core.filter.TokenAuthenticationFilter;
import top.kwseeker.msa.action.security.core.handler.exception.UnauthorizedResponseAuthenticationEntryPoint;
import top.kwseeker.msa.action.security.core.handler.exception.ForbiddenResponseAccessDeniedHandler;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.Filter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
//@Order(2147483641)    //在默认的过滤器链之前创建(默认过滤器链的优先级是2147483642)，但是并没有效，加在Bean方法上也无效，待研究
@AutoConfiguration
//@AutoConfigureBefore(SecurityAutoConfiguration.class)
@AutoConfigureBefore(ManagementWebSecurityAutoConfiguration.class)  //调试了下Spring源码发现默认的过滤器链是被ManagementWebSecurityAutoConfiguration注册进去的不是SecurityAutoConfiguration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true) //TODO
public class MsaWebSecurityConfigurerAdapter {  //旧版本是通过 WebSecurityConfigurerAdapter 这个类配置过滤器链等组件的，新版本已经弃用
                                                //这里借助这个名字表达做的一样的事

    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private MsaSecurityProperties msaSecurityProperties;
    @Resource
    private TokenAuthenticationFilter tokenAuthenticationFilter;

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new UnauthorizedResponseAuthenticationEntryPoint();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new ForbiddenResponseAccessDeniedHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(msaSecurityProperties.getPasswordEncoderLength());
    }

    /**
     * 这里安全认证代码是从starter抽离的
     * AuthorizeRequestsCustomizer本是用于各个项目自己特殊的权限控制配置
     */
    @Bean("specialAuthorizeRequestsCustomizer")
    public AuthorizeRequestsCustomizer specialAuthorizeRequestsCustomizer() {
        return new AuthorizeRequestsCustomizer() {
            @Override
            public void customize(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry) {
                // Swagger 接口文档
                registry.antMatchers("/v3/api-docs/**").permitAll() // 元数据
                        .antMatchers("/swagger-ui.html").permitAll(); // Swagger UI
                // Druid 监控
                registry.antMatchers("/druid/**").anonymous();
                // Spring Boot Actuator 的安全配置
                registry.antMatchers("/actuator").anonymous()
                        .antMatchers("/actuator/**").anonymous();
            }
        };
    }

    /**
     * 创建过滤器链
     * HttpSecurity 其实是一组过滤器的Builder, Spring Security 自动配置阶段会自动创建 HttpSecurity Bean, 里面还包含了一些默认注册的过滤器链
     * 这里我们组要就是定制并注册额外的的过滤器Builder 到 HttpSecurity
     */
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        //注册额外的过滤器Builder
        httpSecurity
                // 开启跨域，注意会和 web 中自定义配置的 CORSFilter 冲突，二选一
                .cors().and()
                // 基于Token，不使用Cookie、Session机制，没有CSRF风险
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 关闭 X-Content-Type-Options 防护
                .headers().frameOptions().disable().and()
                // 异常处理
                .exceptionHandling()
                    // SpringSecurityException异常处理器，只是表示未通过身份验证，因此服务器需要发回一个响应，指示必须进行身份验证，或重定向到特定的web页面
                    .authenticationEntryPoint(authenticationEntryPoint())
                    // AccessDeniedException 异常处理器，访问被拒绝
                    .accessDeniedHandler(accessDeniedHandler()).and()
                .rememberMe();

        // 获得 @PermitAll 带来的 URL 列表，免登录
        Multimap<HttpMethod, String> permitAllUrls = getPermitAllUrlsFromAnnotations();
        // 设置每个请求的权限
        httpSecurity
                // ①：全局共享规则
                .authorizeRequests()
                    // 1.1 静态资源，可匿名访问
                    .antMatchers(HttpMethod.GET, "/*.html", "/**/*.html", "/**/*.css", "/**/*.js").permitAll()
                    // 1.2 设置了 @PermitAll 无需认证，TODO 为何这么麻烦 Spring Security 本身没有处理 @PermitAll注解么
                    .antMatchers(HttpMethod.GET, permitAllUrls.get(HttpMethod.GET).toArray(new String[0])).permitAll()
                    .antMatchers(HttpMethod.POST, permitAllUrls.get(HttpMethod.POST).toArray(new String[0])).permitAll()
                    .antMatchers(HttpMethod.PUT, permitAllUrls.get(HttpMethod.PUT).toArray(new String[0])).permitAll()
                    .antMatchers(HttpMethod.DELETE, permitAllUrls.get(HttpMethod.DELETE).toArray(new String[0])).permitAll()
                    // 1.3 配置文件中 auth.security.permit-all-urls 指定的url无需权限校验
                    .antMatchers(msaSecurityProperties.getPermitAllUrls().toArray(new String[0])).permitAll()
                    // 1.4 设置 App API (都是内部接口，比如 FeignClient 接口) 无需权限校验
                    //.antMatchers(buildAppApi("/**")).permitAll()
                    .and()
                // ②：每个项目的自定义规则（这个本来是starter中的代码被各个服务引用，通过authorizeRequestsCustomizers加载各自的特殊规则）
                //.authorizeRequests(registry -> // 下面，循环设置自定义规则
                // authorizeRequestsCustomizers.forEach(customizer -> customizer.customize(registry))
                //)
                .authorizeRequests(specialAuthorizeRequestsCustomizer())
                // ③：兜底规则，必须认证
                .authorizeRequests()
                    .anyRequest().authenticated()
        ;

        // 在 UsernamePasswordAuthenticationFilter 前面添加 Token Filter
        // TODO tokenAuthenticationFilter 认证成功后会跳过 UsernamePasswordAuthenticationFilter 么？
        httpSecurity.addFilterBefore((Filter) tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    /**
     * 获取 RequestMappingHandlerMapping 中所有免权限验证的 URL，免权限认证的 URL 方法上注释有 @PermitAll 注解
     * @return HttpMethod -> url 的 Map
     */
    private Multimap<HttpMethod, String> getPermitAllUrlsFromAnnotations() {
        Multimap<HttpMethod, String> result = HashMultimap.create();
        // 获得接口对应的 Handler先通过RootBeanDefinition找顺序，没有再通过当前对象找Method 集合
        RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping)
                applicationContext.getBean("requestMappingHandlerMapping");
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = requestMappingHandlerMapping.getHandlerMethods();
        // 获得有 @PermitAll 注解的接口
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethodMap.entrySet()) {
            HandlerMethod handlerMethod = entry.getValue();
            if (!handlerMethod.hasMethodAnnotation(PermitAll.class)) {
                continue;
            }

            //注意SpringMVC不同版本差异
            //旧版本路径信息在 RequestMappingInfo.patternsCondition 中存储
            //新版本路径信息在 RequestMappingInfo.pathPatternsCondition 中存储
            //这里应该兼容一下
            PatternsRequestCondition patternsCondition = entry.getKey().getPatternsCondition();
            PathPatternsRequestCondition pathPatternsCondition = entry.getKey().getPathPatternsCondition();
            if (patternsCondition == null && pathPatternsCondition == null) {
                continue;
            }
            Set<String> urls = new HashSet<>();
            if (patternsCondition != null) {
                urls.addAll(patternsCondition.getPatterns());
            }
            if (pathPatternsCondition != null) {
                urls.addAll(pathPatternsCondition.getPatternValues());
            }

            // 根据请求方法，添加到 result 结果
            entry.getKey().getMethodsCondition().getMethods().forEach(requestMethod -> {
                switch (requestMethod) {
                    case GET:
                        result.putAll(HttpMethod.GET, urls);
                        break;
                    case POST:
                        result.putAll(HttpMethod.POST, urls);
                        break;
                    case PUT:
                        result.putAll(HttpMethod.PUT, urls);
                        break;
                    case DELETE:
                        result.putAll(HttpMethod.DELETE, urls);
                        break;
                }
            });
        }
        log.debug("PermitAll request urls: {}", result);
        return result;
    }
}
