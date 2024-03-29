package top.kwseeker.msa.mall.trigger.mq.event;

import org.apache.commons.lang3.RandomStringUtils;
import top.kwseeker.msa.mall.types.mq.event.BaseEvent;
import top.kwseeker.msa.mall.domain.activity.model.entity.ActivitySyncStockEntity;

import java.util.Date;

public class ActivitySyncStockMessageEvent extends BaseEvent<ActivitySyncStockEntity> {

    public static final String TOPIC = "activity_sync_stock";

    @Override
    public EventMessage<ActivitySyncStockEntity> buildEventMessage(ActivitySyncStockEntity data) {
        return EventMessage.<ActivitySyncStockEntity>builder()
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