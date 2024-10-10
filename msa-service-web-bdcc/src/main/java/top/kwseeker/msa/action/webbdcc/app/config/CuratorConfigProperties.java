package top.kwseeker.msa.action.webbdcc.app.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "zookeeper.curator.config", ignoreInvalidFields = true)
public class CuratorConfigProperties {

    private String connectString;
    //重试睡眠时间，或者说重试间隔
    private int baseSleepTimeMs;
    private int maxRetries;
    private int sessionTimeoutMs;
    private int connectionTimeoutMs;
}
