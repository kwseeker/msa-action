package top.kwseeker.msa.mall.infrastructure.dao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan(basePackages = {"top.kwseeker.msa.mall.infrastructure.dao"})
@ComponentScan(basePackages = {"top.kwseeker.msa.mall.infrastructure"})
public class MockApplication {
}
