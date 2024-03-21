package top.kwseeker.msa.kafka.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import top.kwseeker.msa.kafka.types.BaseEvent;

import java.util.Date;

public class UserMessageEvent extends BaseEvent<UserMessageEvent.UserMessage> {

    @Value("${kafka.topic.name}")
    private String topic;

    @Override
    public EventMessage<UserMessage> buildEventMessage(UserMessage data) {
        return EventMessage.<UserMessage>builder()
                .id(RandomStringUtils.randomNumeric(11))
                .timestamp(new Date())
                .data(data)
                .build();
    }

    @Override
    public String topic() {
        return topic;
    }

    /**
     * 要推送的事件消息，聚合到当前类下
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserMessage {
        private String userId;
        private String userName;
        private String userType;
    }
}
