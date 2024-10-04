package top.kwseeker.msa.action.rpc.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import top.kwseeker.msa.action.rpc.client.RpcClient;

@AutoConfiguration
public class MsaRpcAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RpcClient rpcClient() {
        return new RpcClient();
    }
}
