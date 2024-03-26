package top.kwseeker.msa.mall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients(basePackages = "top.kwseeker.msa.**.api.feign")   //多模块项目可能扫描不到@FeignClient，需要指定扫描路径
@SpringBootApplication
public class MallServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallServiceApplication.class, args);
    }
}
