package top.kwseeker.msa.action.basicone.trigger.rpc;

import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import top.kwseeker.msa.action.basicone.api.grpc.proto.AccountServiceGrpc;
import top.kwseeker.msa.action.basicone.api.grpc.proto.PaymentRequest;
import top.kwseeker.msa.action.basicone.api.grpc.proto.PaymentResponse;
import top.kwseeker.msa.action.basicone.domain.account.model.PayResult;
import top.kwseeker.msa.action.basicone.domain.account.model.Payment;
import top.kwseeker.msa.action.basicone.domain.account.service.AccountService;
import top.kwseeker.msa.action.rpc.filter.GrpcServerFilter;

import javax.annotation.Resource;

/**
 * 这个是胶水类，用于将 protoc 生成的 Netty 服务端（位于AccountServiceGrpc）与实际 Service 进行绑定
 */
@GRpcService(interceptors = {GrpcServerFilter.class}) //@Service
public class AccountRpcService extends AccountServiceGrpc.AccountServiceImplBase {

    @Resource
    private AccountService accountService;

    @Override
    public void pay(PaymentRequest request, StreamObserver<PaymentResponse> responseObserver) {
        Payment payment = new Payment();
        payment.setUserId(request.getUserId());
        payment.setOrderId(request.getOrderId());
        payment.setAmount(request.getAmount());

        PayResult pay = accountService.pay(payment);

        PaymentResponse response = PaymentResponse.newBuilder()
                .setResult(pay.getCode())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
