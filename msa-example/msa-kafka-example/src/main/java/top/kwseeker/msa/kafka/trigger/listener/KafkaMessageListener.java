package top.kwseeker.msa.kafka.trigger.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class KafkaMessageListener {

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "${kafka.topic.group}", concurrency = "1")
    public void listen(ConsumerRecord<?, ?> record,
                           Acknowledgment ack,
                           @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Optional<?> message = Optional.ofNullable(record.value());
        if (message.isPresent()) {
            Object msg = message.get();
            try {
                // 逻辑处理

                // 确认消息消费完成，如果抛异常消息会进入重试
                ack.acknowledge();
                System.out.println("Kafka消费成功! Topic:" + topic + ", Message:" + msg);
            } catch (Exception e) {
                System.out.println("Kafka消费失败！Topic:" + topic + ", Message:" + msg);
                e.printStackTrace();
            }
        }
    }
}
