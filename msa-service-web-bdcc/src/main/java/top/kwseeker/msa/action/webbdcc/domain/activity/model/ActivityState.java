package top.kwseeker.msa.action.webbdcc.domain.activity.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ActivityState {
    NOT_STARTED("活动未开始"),
    IN_PROGRESS("活动进行中"),
    ENDED("活动已结束");

    private final String description;
}
