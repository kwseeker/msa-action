package top.kwseeker.msa.config.casbin;

import org.casbin.annotation.CasbinDataSource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(CasbinDataSourceProperties.class)
public class CasbinConfiguration {

    @Bean
    @CasbinDataSource
    public DataSource casbinDataSource(CasbinDataSourceProperties properties) {
        return DataSourceBuilder.create()
                .url(properties.getUrl())
                .driverClassName(properties.getDriverClassName())
                .username(properties.getUsername())
                .password(properties.getPassword()).build();
    }
}
