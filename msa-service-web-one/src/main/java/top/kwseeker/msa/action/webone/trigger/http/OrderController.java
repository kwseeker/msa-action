package top.kwseeker.msa.action.webone.trigger.http;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.kwseeker.msa.action.framework.common.model.Response;
import top.kwseeker.msa.action.webone.domain.order.service.OrderService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @PostMapping("/pay")
    public Response<Integer> pay(@RequestParam(value = "userId") String userId,
                                     @RequestParam(value = "orderId") String orderId,
                                     @RequestParam(value = "amount") int amount) {
        Integer res = orderService.pay(userId, orderId, amount);
        return Response.success(res);
    }
}
