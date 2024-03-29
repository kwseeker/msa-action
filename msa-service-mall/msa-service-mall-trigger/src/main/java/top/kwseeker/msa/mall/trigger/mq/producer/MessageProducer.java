package top.kwseeker.msa.mall.trigger.mq.producer;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;
import top.kwseeker.msa.mall.types.mq.event.BaseEvent;
import top.kwseeker.msa.mall.domain.activity.model.entity.ActivityDeliveryItemEntity;
import top.kwseeker.msa.mall.domain.activity.model.entity.ActivitySyncStockEntity;
import top.kwseeker.msa.mall.domain.activity.mq.IMessageProducer;
import top.kwseeker.msa.mall.trigger.mq.event.ActivityDeliveryItemMessageEvent;
import top.kwseeker.msa.mall.trigger.mq.event.ActivitySyncStockMessageEvent;

import javax.annotation.Resource;

@Slf4j
@Component
public class MessageProducer implements IMessageProducer {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void publishActivitySyncStockMessage(ActivitySyncStockEntity activitySyncStockEntity) {
        BaseEvent.EventMessage<ActivitySyncStockEntity> message = new ActivitySyncStockMessageEvent()
                .buildEventMessage(activitySyncStockEntity);
        publish(ActivitySyncStockMessageEvent.TOPIC, message);
    }

    @Override
    public void publishActivityDeliveryItemMessage(ActivityDeliveryItemEntity activityDeliveryItemEntity) {
        BaseEvent.EventMessage<ActivityDeliveryItemEntity> message = new ActivityDeliveryItemMessageEvent()
                .buildEventMessage(activityDeliveryItemEntity);
        publish(ActivityDeliveryItemMessageEvent.TOPIC, message);
    }

    private void publish(String topic, BaseEvent.EventMessage<?> eventMessage) {
        try {
            String messageJson = JSON.toJSONString(eventMessage);
            log.info("发送MQ消息 topic:{} message:{}", topic, messageJson);
            ListenableFuture<SendResult<String, String>> send = kafkaTemplate.send(topic, messageJson);
            send.addCallback(new SuccessCallback<SendResult<String, String>>() {
                @Override
                public void onSuccess(SendResult<String, String> result) {
                    log.info("发送MQ消息成功, result:{}", result.toString());
                }
            }, new FailureCallback() {
                @Override
                public void onFailure(Throwable e) {
                    log.error("发送MQ消息失败", e);
                }
            });
        } catch (Exception e) {
            log.error("发送MQ消息失败 topic:{} message:{}", topic, JSON.toJSONString(eventMessage));
        }
    }
}