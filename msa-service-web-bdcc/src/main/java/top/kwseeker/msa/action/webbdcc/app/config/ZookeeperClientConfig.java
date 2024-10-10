package top.kwseeker.msa.action.webbdcc.app.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ZK Curator Client 配置
 */
@Configuration
@EnableConfigurationProperties(CuratorConfigProperties.class)
public class ZookeeperClientConfig {

    @Bean(name = "zookeeperClient")
    public CuratorFramework curatorFramework(CuratorConfigProperties properties) {
        ExponentialBackoffRetry backoffRetry = new ExponentialBackoffRetry(properties.getBaseSleepTimeMs(),
                properties.getMaxRetries());
        CuratorFramework zkClient = CuratorFrameworkFactory.builder()
                .connectString(properties.getConnectString())
                .sessionTimeoutMs(properties.getSessionTimeoutMs())
                .connectionTimeoutMs(properties.getConnectionTimeoutMs())
                .retryPolicy(backoffRetry)
                .build();
        zkClient.start();
        return zkClient;
    }
}
