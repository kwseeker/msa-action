package top.kwseeker.msa.action.webone.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.kwseeker.msa.action.framework.common.model.Response;

@FeignClient(value = "msa-service-basic-one", contextId = "helloAPI", path = "/basic-one")
public interface HelloApiClient {

    @GetMapping("/hello")
    Response<String> sayHello(@RequestParam("name") String name);
}
