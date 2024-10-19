package top.kwseeker.msa.action.webha.domain.ha.service;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloBlockedService {

    /**
     * blockHandler 方法需要在原方法参数基础上新增一个 BlockException 参数
     */
    public String testFlow(String name, BlockException e) {
        log.info("testFlow: {} been blocked, e", name, e);
        return "Hello";
    }
}
