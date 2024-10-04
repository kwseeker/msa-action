package top.kwseeker.msa.action.rpc.filter;

import io.grpc.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GrpcServerFilter implements ServerInterceptor {

    @Override
    public <R, P> ServerCall.Listener<R> interceptCall(ServerCall<R, P> serverCall,
                                                                 Metadata metadata,
                                                                 ServerCallHandler<R, P> serverCallHandler) {
        log.debug("执行GRPC服务端对请求拦截GrpcServerFilter");
        log.debug("metadata = " + metadata);
        return serverCallHandler.startCall(new ForwardingServerCall.SimpleForwardingServerCall<R, P>(serverCall) {
            @Override
            public void sendMessage(final P message) {
                //...
                log.debug("RPC Sever response: " + message);
                super.sendMessage(message);
            }
        }, metadata);
    }
}
