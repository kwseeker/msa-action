package top.kwseeker.msa.mybatis.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@AutoConfiguration
@EnableTransactionManagement(proxyTargetClass = true)
public class MsaMybatisAutoConfiguration {
}
