package top.kwseeker.msa.action.webone.trigger.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.kwseeker.msa.action.framework.common.model.Response;
import top.kwseeker.msa.action.webone.api.feign.HelloApiClient;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping
public class HelloController {

    @Resource
    private HelloApiClient helloApiClient;

    @GetMapping("/hello")
    public Response<String> sayHello(@RequestParam("name") String name) {
        log.info("sayHello: {}", name);
        Response<String> resp = helloApiClient.sayHello(name);
        return Response.success(resp.getData());
    }
}
