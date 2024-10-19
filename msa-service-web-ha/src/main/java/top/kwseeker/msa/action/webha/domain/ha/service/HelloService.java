package top.kwseeker.msa.action.webha.domain.ha.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.stereotype.Service;

@Service
public class HelloService {

    @SentinelResource(value = "HelloService::testFlow",
            blockHandler = "testFlow", blockHandlerClass = {HelloBlockedService.class},
            fallback = "testBlockedFlow"
    )
    public String testFlow(String name) {
        return "Hello " + name;
    }

    public String testBlockedFlow(String name, Throwable e) {
        return "Hello " + name + " , by fallback for blocked";
    }

    public String testFuse(String name) {
        return "Hello " + name;
    }
}
