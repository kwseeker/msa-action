package top.kwseeker.msa.action.webone.domain.order.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.kwseeker.msa.action.webone.api.rpc.AccountRpcClient;

import javax.annotation.Resource;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private AccountRpcClient accountRpcClient;

    @Override
    public Integer pay(String userId, String orderId, Integer amount) {
        log.info("模拟订单支付前检查...");
        log.info("订单支付...");
        return accountRpcClient.pay(userId, orderId, amount);
    }
}
