package top.kwseeker.msa.action.rpc.client;

import io.grpc.stub.AbstractStub;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * RPC 客户端实现，本质是动态代理生成的 Netty 客户端
 */
@Slf4j
public class RpcClient {

    public <T, S extends AbstractStub<S>> T syncInvoke(final AbstractStub<S> abstractStub,
                                                       final String method,
                                                       final Object param,
                                                       final Class<T> clazz) {
        // TODO 加缓存优化方法查找
        for (Method m : abstractStub.getClass().getMethods()) {
            if (m.getName().equals(method)) {
                try {
                    Object res = m.invoke(abstractStub, m.getParameterTypes()[0].cast(param));
                    log.debug("RpcClient syncInvoke: res=" + res);
                    return (T) res;
                } catch (IllegalAccessException | InvocationTargetException | ClassCastException e) {
                    log.error("failed to invoke grpc server", e);
                    throw new RuntimeException(e);
                }
            }
        }
        throw new RuntimeException("not found method: " + method);
    }
}
