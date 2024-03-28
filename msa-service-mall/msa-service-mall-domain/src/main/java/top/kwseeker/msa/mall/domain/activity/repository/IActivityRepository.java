package top.kwseeker.msa.mall.domain.activity.repository;

import top.kwseeker.msa.mall.domain.activity.model.entity.ActivitySetEntity;
import top.kwseeker.msa.mall.domain.activity.model.vo.ActivityStockVO;

public interface IActivityRepository {

    Integer setActivity(ActivitySetEntity activitySetEntity);

    ActivitySetEntity getActivitySetting(Integer activityId);

    /**
     * 获取活动物品库存信息，注意返回值不是剩余库存，而是已派发数量
     *
     * @return 已派发数量
     */
    Integer getActivityStock(Integer activityId);

    /**
     * 查询用户活动参与记录
     *
     * @param activityId    活动ID
     * @param userId        用户ID
     * @return              是否已参与
     */
    boolean getUserPartakeRecord(Integer activityId, Long userId);

    /**
     * 添加用户活动参与记录
     */
    void addUserPartakeRecord(Integer activityId, Long userId);

    ActivityStockVO subtractActivityStock(ActivitySetEntity activitySetEntity, Long userId);
}
