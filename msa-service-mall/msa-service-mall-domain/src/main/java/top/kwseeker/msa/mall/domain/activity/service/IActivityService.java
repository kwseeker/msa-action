package top.kwseeker.msa.mall.domain.activity.service;

import top.kwseeker.msa.mall.domain.activity.model.entity.ActivityDrawEntity;
import top.kwseeker.msa.mall.domain.activity.model.vo.ActivityDrawResultVO;

public interface IActivityService {

    /**
     * 领取活动物品
     */
    ActivityDrawResultVO drawItem(ActivityDrawEntity drawEntity);
}
