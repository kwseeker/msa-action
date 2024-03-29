package top.kwseeker.msa.mall.trigger.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 参与活动领取物品请求对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDrawDTO {

    private Integer activityId;
}
