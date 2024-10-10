package top.kwseeker.msa.action.webbdcc.api.dto;

import lombok.Data;
import top.kwseeker.msa.action.webbdcc.domain.activity.model.ClassicActivity;

@Data
public class ActivitySettingDTO {

    private Integer enable;
    private ClassicActivity activity;
}
