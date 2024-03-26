package top.kwseeker.msa.mall.infrastructure.dao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"top.kwseeker.msa.mall.infrastructure.dao"})
public class MockApplication {
}
