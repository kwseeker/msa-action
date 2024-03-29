package top.kwseeker.msa.mall.domain.activity.model.entity;

import lombok.Data;

@Data
public class ActivitySyncStockEntity {

    private Integer activityId;
    private Integer usedCount;
    private Long userId;
}
