package top.kwseeker.msa.mall.trigger.model.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 参与活动领取物品请求对象
 */
@Data
@Builder
public class ActivityDrawDTO {

    private Integer activityId;
}
