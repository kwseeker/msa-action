package top.kwseeker.msa.kafka.infrastructure.event;

import com.alibaba.fastjson.JSON;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;
import top.kwseeker.msa.kafka.types.BaseEvent;

import javax.annotation.Resource;

@Component
public class EventPublisher {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    public void publish(String topic, BaseEvent.EventMessage<?> eventMessage) {
        try {
            String messageJson = JSON.toJSONString(eventMessage);
            System.out.printf("发送MQ消息 topic:%s message:%s\n", topic, messageJson);
            ListenableFuture<SendResult<String, String>> send = kafkaTemplate.send(topic, messageJson);
            send.addCallback(new SuccessCallback<SendResult<String, String>>() {
                @Override
                public void onSuccess(SendResult<String, String> result) {
                    System.out.println("发送MQ消息成功, result" + result.toString());
                }
            }, new FailureCallback() {
                @Override
                public void onFailure(Throwable e) {
                    System.out.println("发送MQ消息失败");
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            System.out.printf("发送MQ消息失败 topic:%s message:%s\n", topic, JSON.toJSONString(eventMessage));
            throw e;
        }
    }
}
