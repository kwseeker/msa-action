package top.kwseeker.msa.action.webbdcc.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 演示使用 ZK Nacos 实现业务动态配置中心
 */
@ComponentScan(basePackages = {"top.kwseeker.msa.action.webbdcc"})
@SpringBootApplication
public class BusinessDynamicConfigCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusinessDynamicConfigCenterApplication.class, args);
    }
}
