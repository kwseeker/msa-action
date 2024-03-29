package top.kwseeker.msa.mall.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivitySetEntity {

    private Integer activityId;
    private String name;
    private Integer itemId;
    private Integer stock;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String creator;
}
