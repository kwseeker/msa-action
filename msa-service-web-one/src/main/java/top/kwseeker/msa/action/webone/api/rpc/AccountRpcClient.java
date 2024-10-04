package top.kwseeker.msa.action.webone.api.rpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Component;
import top.kwseeker.msa.action.basicone.api.grpc.proto.AccountServiceGrpc;
import top.kwseeker.msa.action.basicone.api.grpc.proto.PaymentRequest;
import top.kwseeker.msa.action.basicone.api.grpc.proto.PaymentResponse;
import top.kwseeker.msa.action.rpc.client.RpcClient;
import top.kwseeker.msa.action.rpc.filter.GrpcClientFilter;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class AccountRpcClient {

    // protoc 生成的 RPC 接口
    private AccountServiceGrpc.AccountServiceBlockingStub accountServiceBlockingStub;
    // Netty 客户端
    @Resource
    private RpcClient rpcClient;

    @PostConstruct
    private void init() {
        // TODO 接入 Nacos: 即通过服务名获取任意一个服务实例的IP和端口
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 38201)
                .intercept(new GrpcClientFilter())
                .usePlaintext()
                .build();
        accountServiceBlockingStub = AccountServiceGrpc.newBlockingStub(managedChannel);
    }

    public Integer pay(String userId, String orderId, Integer amount) {
        PaymentRequest request = PaymentRequest.newBuilder()
                .setUserId(userId).setOrderId(orderId).setAmount(amount)
                .build();
        PaymentResponse response = rpcClient.syncInvoke(accountServiceBlockingStub, "pay", request, PaymentResponse.class);
        return response.getResult();
    }
}
