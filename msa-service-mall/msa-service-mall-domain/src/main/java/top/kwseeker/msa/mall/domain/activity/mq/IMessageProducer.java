package top.kwseeker.msa.mall.domain.activity.mq;

import top.kwseeker.msa.mall.domain.activity.model.entity.ActivityDeliveryItemEntity;
import top.kwseeker.msa.mall.domain.activity.model.entity.ActivitySyncStockEntity;

public interface IMessageProducer {

    void publishActivitySyncStockMessage(ActivitySyncStockEntity activitySyncStockEntity);

    void publishActivityDeliveryItemMessage(ActivityDeliveryItemEntity activityDeliveryItemEntity);
}
