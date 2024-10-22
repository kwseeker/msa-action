package top.kwseeker.msa.action.webha.trigger.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.kwseeker.msa.action.framework.common.context.UserContext;
import top.kwseeker.msa.action.framework.common.model.Response;
import top.kwseeker.msa.action.sentinel.context.UserContextUtil;
import top.kwseeker.msa.action.webha.domain.ha.service.HelloService;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping
public class HelloController {

    @Resource
    private HelloService helloService;

    /**
     * 流控案例
     * 限流后抛出 FlowException
     * 通过 {@link <a href="https://example.com/">Sentinel Dashboard</a>} 设置流控规则
     * 比如设置单机QPS阈值5，超过阈值后快速失败
     */
    @GetMapping("/flow")
    public Response<String> testFlow(@RequestParam("name") String name) {
        log.info("testFlow: {}", name);
        String res = helloService.testFlow(name);
        return Response.success(res);
    }

    /**
     * 测试通过 @SentinelResource 定义 Sentinel 资源，以及使用自定义 BlockException 处理器、Fallback 实现
     * 使用此注解校验逻辑是在 SentinelResourceAspect 代理类中执行
     */
    @GetMapping("/flow1")
    public Response<String> testFlow1(@RequestParam("name") String name) {
        log.info("testFlow1: {}", name);
        String res = helloService.testFlow(name);
        return Response.success(res);
    }

    /**
     * 熔断案例
     * 熔断后抛出 DegradeException
     * 比如设置慢调用比例超过10%或异常比例超过10%，触发熔断
     */
    @GetMapping("/fuse")
    public Response<String> testFuse(@RequestParam("name") String name) {
        log.info("testFuse: {}", name);
        return Response.success("Hello " + name);
    }

    /**
     * 热点参数限流案例
     * 系统自适应限流规则针对所有资源
     */
    @GetMapping("/hot_param")
    public Response<String> testHotParam(@RequestParam("userId") String userId) {
        log.info("testHotParam: {}", userId);
        return Response.success("Hello " + userId);
    }

    /**
     * 拓展的用户黑白名单限流规则测试
     */
    @GetMapping("/user_authority")
    public Response<String> testUserAuthority() {
        UserContext userContext = UserContextUtil.getUserContext();
        log.info("testUserAuthority: {}", userContext);
        return Response.success("Hello " + userContext.getUsername());
    }
}
