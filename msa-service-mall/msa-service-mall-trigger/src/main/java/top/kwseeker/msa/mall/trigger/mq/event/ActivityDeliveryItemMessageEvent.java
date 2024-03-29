package top.kwseeker.msa.mall.trigger.mq.event;

import org.apache.commons.lang3.RandomStringUtils;
import top.kwseeker.msa.mall.types.mq.event.BaseEvent;
import top.kwseeker.msa.mall.domain.activity.model.entity.ActivityDeliveryItemEntity;

import java.util.Date;

public class ActivityDeliveryItemMessageEvent extends BaseEvent<ActivityDeliveryItemEntity> {

    public static final String TOPIC = "activity_delivery_item";

    @Override
    public EventMessage<ActivityDeliveryItemEntity> buildEventMessage(ActivityDeliveryItemEntity data) {
        return EventMessage.<ActivityDeliveryItemEntity>builder()
                .id(RandomStringUtils.randomNumeric(11))
                .timestamp(new Date())
                .data(data)
                .build();
    }

    @Override
    public String topic() {
        return TOPIC;
    }
}