package top.kwseeker.msa.action.webha.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 演示高可用Web
 * 包括：
 *  限流、熔断、降级
 */
@ComponentScan(basePackages = {"top.kwseeker.msa.action.webha"})
@SpringBootApplication
public class HighAvailableWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(HighAvailableWebApplication.class, args);
    }
}
