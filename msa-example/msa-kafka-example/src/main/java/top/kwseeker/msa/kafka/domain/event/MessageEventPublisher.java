package top.kwseeker.msa.kafka.domain.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import top.kwseeker.msa.kafka.types.BaseEvent;

import javax.annotation.Resource;

@Component
public class MessageEventPublisher {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    public void publish(String topic, BaseEvent.EventMessage<?> eventMessage) {

    }
}
