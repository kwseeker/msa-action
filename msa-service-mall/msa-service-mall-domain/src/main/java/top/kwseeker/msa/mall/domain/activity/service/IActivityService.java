package top.kwseeker.msa.mall.domain.activity.service;

import top.kwseeker.msa.mall.domain.activity.model.entity.ActivityDrawEntity;
import top.kwseeker.msa.mall.domain.activity.model.entity.ActivitySetEntity;
import top.kwseeker.msa.mall.domain.activity.model.vo.ActivityDrawResultVO;

public interface IActivityService {

    /**
     * 领取活动物品
     */
    ActivityDrawResultVO drawItem(ActivityDrawEntity drawEntity);

    /**
     * 模拟活动设置
     */
    Integer setActivity(ActivitySetEntity activitySetEntity);
}
