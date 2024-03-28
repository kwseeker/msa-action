package top.kwseeker.msa.mall.infrastructure.repository;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisClientConfig {

    @Bean("redissonClient")
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://127.0.0.1:6379")
                //.setPassword(properties.getPassword())
                .setConnectionPoolSize(10)
                .setConnectionMinimumIdleSize(5)
                .setIdleConnectionTimeout(30000)
                .setConnectTimeout(5000)
                .setRetryAttempts(3)
                .setRetryInterval(1000)
                .setPingConnectionInterval(60000)
                .setKeepAlive(true);
        return Redisson.create(config);
    }
}