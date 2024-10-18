package top.kwseeker.msa.action.webha.trigger.http;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.kwseeker.msa.action.framework.common.model.Response;
import top.kwseeker.msa.action.webha.domain.ha.service.HaTestService;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping
public class HelloController {

    @Resource
    private HaTestService haTestService;

    /**
     * 流控案例
     * 通过 {@link <a href="https://example.com/">Sentinel Dashboard</a>} 设置流控规则
     * 比如设置单机QPS阈值5，超过阈值后快速失败
     */
    @GetMapping("/flow")
    public Response<String> testFlow(@RequestParam("name") String name) {
        log.info("testFlow: {}", name);
        String res = haTestService.testFlow(name);
        return Response.success(res);
    }

    /**
     * 测试通过 @SentinelResource 定义 Sentinel 资源，以及使用自定义 BlockException 处理器、Fallback 实现
     * 使用此注解校验逻辑是在 SentinelResourceAspect 代理类中执行
     */
    @GetMapping("/flow1")
    @SentinelResource(value = "/flow1",
            blockHandler = "testFlow1", blockHandlerClass = {HelloBlockedController.class}
            //, fallback = "testFlow1Fallback", fallbackClass = {DefaultFallback.class} //感觉 fallback 和 统一异常处理定位相同
    )
    public Response<String> testFlow1(@RequestParam("name") String name) {
        log.info("testFlow1: {}", name);
        String res = haTestService.testFlow(name);
        return Response.success(res);
    }

    /**
     * 熔断案例
     * 这里通过 {@link <a href="https://example.com/">Sentinel Dashboard</a>} 设置熔断规则
     * 比如设置慢调用比例超过10%或异常比例超过10%，触发熔断
     */
    @GetMapping("/fuse")
    public Response<String> testFuse(@RequestParam("name") String name) {
        log.info("testFuse: {}", name);
        return Response.success("Hello " + name);
    }
}