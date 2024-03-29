package top.kwseeker.msa.mall.trigger.mq.consumer;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import top.kwseeker.msa.mall.domain.activity.model.entity.ActivitySyncStockEntity;
import top.kwseeker.msa.mall.types.mq.event.BaseEvent;
import top.kwseeker.msa.mall.domain.activity.model.entity.ActivityDeliveryItemEntity;
import top.kwseeker.msa.mall.domain.activity.service.IActivityService;
import top.kwseeker.msa.mall.trigger.mq.event.ActivityDeliveryItemMessageEvent;

import javax.annotation.Resource;
import java.util.Optional;

@Slf4j
@Component
public class ActivityDeliveryItemMessageConsumer {

    @Resource
    private IActivityService activityService;

    @KafkaListener(topics = {ActivityDeliveryItemMessageEvent.TOPIC},
            groupId = "${kafka.topic.group}", concurrency = "1")
    public void listen(ConsumerRecord<?, ?> record,
                           Acknowledgment ack,
                           @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Optional<?> message = Optional.ofNullable(record.value());
        if (!message.isPresent()) {
            return;
        }
        log.info("消费MQ消息: 活动商品发货, Topic:{}, Message:{}", topic, message.get());

        BaseEvent.EventMessage<?> eventMessage = JSON.parseObject((String) message.get(), BaseEvent.EventMessage.class);
        String messageId = eventMessage.getId();
        //通过消息ID检查消息是否已经消费过, TODO

        ActivityDeliveryItemEntity activityDeliveryItemEntity = JSON.to(ActivityDeliveryItemEntity.class, eventMessage.getData());
        //同步活动已使用库存
        activityService.deliveryItem(activityDeliveryItemEntity);
        // 确认消息消费完成，如果抛异常消息会进入重试
        ack.acknowledge();
    }
}