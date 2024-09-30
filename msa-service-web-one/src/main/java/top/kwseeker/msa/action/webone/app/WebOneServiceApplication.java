package top.kwseeker.msa.action.webone.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients(basePackages = "top.kwseeker.msa.action.webone.api.feign")
@ComponentScan(basePackages = {"top.kwseeker.msa.action.webone"})
public class WebOneServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebOneServiceApplication.class, args);
    }
}