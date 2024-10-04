package top.kwseeker.msa.action.webone.domain.order.service;

public interface OrderService {

    Integer pay(String userId, String orderId, Integer amount);
}