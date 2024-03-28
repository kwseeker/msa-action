package top.kwseeker.msa.mall.trigger.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActivitySetDTO {

    private Integer itemId;
    private Integer stock;
}
