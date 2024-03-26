package top.kwseeker.msa.mall.domain.activity.model.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActivityDrawResultVO {

    private Long userId;
    private Integer itemId;
    private String itemName;
}
