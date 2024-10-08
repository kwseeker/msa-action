package top.kwseeker.msa.action.basicone.trigger.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.kwseeker.msa.action.framework.common.model.Response;

@Slf4j
@RestController
@RequestMapping
public class HelloController {

    @GetMapping("/hello")
    public Response<String> sayHello(@RequestParam("name") String name) {
        log.info("sayHello: {}", name);
        String message = "Hello, " + name;
        return Response.success(message);
    }
}
