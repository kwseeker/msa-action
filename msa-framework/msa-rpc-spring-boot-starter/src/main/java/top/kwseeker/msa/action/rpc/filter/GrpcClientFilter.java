package top.kwseeker.msa.action.rpc.filter;

import io.grpc.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GrpcClientFilter implements ClientInterceptor {

    public static final Metadata.Key<String> EXAM_META_DATA = Metadata.Key.of("exam-meta", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <R, P> ClientCall<R, P> interceptCall(MethodDescriptor<R, P> methodDescriptor, CallOptions callOptions, Channel channel) {
        log.debug("执行GRPC客户端调用拦截GrpcClientFilter");
        String[] clazzNameAndMethod = methodDescriptor.getFullMethodName().split("/");
        log.debug("class: " + clazzNameAndMethod[0]);
        log.debug("method: " + clazzNameAndMethod[1]);

        return new ForwardingClientCall.SimpleForwardingClientCall<R, P>(channel.newCall(methodDescriptor, callOptions)) {
            @Override
            public void start(final Listener<P> responseListener, final Metadata headers) {
                log.debug("headers = " + headers);
                headers.put(EXAM_META_DATA, "test_example_meta_data");
                //...
                super.start(new ForwardingClientCallListener.SimpleForwardingClientCallListener<P>(responseListener) {
                    public void onClose(final Status status, final Metadata trailers) {
                        log.debug("response status: " + status.getCode());
                        //...
                        super.onClose(status, trailers);
                    } }, headers);
            }
        };
    }
}
