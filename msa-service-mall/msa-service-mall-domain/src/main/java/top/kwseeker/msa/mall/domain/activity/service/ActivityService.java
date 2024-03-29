package top.kwseeker.msa.mall.domain.activity.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.kwseeker.msa.action.framework.common.exception.GlobalErrorCodes;
import top.kwseeker.msa.mall.types.exception.MallDomainException;
import top.kwseeker.msa.mall.types.exception.MallErrorCodes;
import top.kwseeker.msa.mall.domain.activity.model.entity.ActivityDeliveryItemEntity;
import top.kwseeker.msa.mall.domain.activity.model.entity.ActivityDrawEntity;
import top.kwseeker.msa.mall.domain.activity.model.entity.ActivitySetEntity;
import top.kwseeker.msa.mall.domain.activity.model.entity.ActivitySyncStockEntity;
import top.kwseeker.msa.mall.domain.activity.model.vo.ActivityDrawResultVO;
import top.kwseeker.msa.mall.domain.activity.model.vo.ActivityStockVO;
import top.kwseeker.msa.mall.domain.activity.mq.IMessageProducer;
import top.kwseeker.msa.mall.domain.activity.repository.IActivityRepository;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Slf4j
@Service
public class ActivityService implements IActivityService {

    @Resource
    private IActivityRepository activityRepository;
    @Resource
    private IMessageProducer messageProducer;

    /**
     * 模拟限量免费送活动的高并发场景处理
     * 1）缓存预热，活动开始前提前将活动时间、物品库存添加到缓存中，可以防止缓存雪崩
     * 2) Redis incr实现扣库存，加锁防止超卖、同时降低锁的粒度提升性能
     * 3）MQ异步执行用户参与记录缓存到数据库的同步，MQ异步发货
     * 4）定时任务定期同步缓存中活动物品库存到数据库
     */
    @Override
    public ActivityDrawResultVO drawItem(ActivityDrawEntity drawEntity) {
        //1 领取前的检查：活动时间、活动物品库存、用户参与记录
        ActivitySetEntity activitySetEntity = doDrawBefore(drawEntity);

        //2 Redis扣减库存
        //  加锁后再检查一次库存、锁key加上库存数量降低锁的粒度、对于抢锁失败的直接返回失败即可（不需要遵守公平性）
        ActivityStockVO activityStockVO = activityRepository.subtractActivityStock(activitySetEntity, drawEntity.getUserId());
        if (GlobalErrorCodes.SUCCESS != activityStockVO.getErrorCode()) {
            throw new MallDomainException(activityStockVO.getErrorCode());
        }

        //3 用户成功抢到物品后更新缓存中用户参与记录
        activityRepository.addUserPartakeRecord(drawEntity.getActivityId(), drawEntity.getUserId());

        //4 如果还有支付流程的话(MQ异步处理)，还需要考虑支付失败，失败的话需要恢复库存，并释放分布式锁stockTokenKey
        //5 MQ异步更新库存到数据库、添加用户活动参与记录到数据库
        ActivitySyncStockEntity activitySyncStockEntity = new ActivitySyncStockEntity();
        activitySyncStockEntity.setActivityId(drawEntity.getActivityId());
        activitySyncStockEntity.setUsedCount(activityStockVO.getStockUsedCount());
        activitySyncStockEntity.setUserId(drawEntity.getUserId());
        messageProducer.publishActivitySyncStockMessage(activitySyncStockEntity);

        //6 MQ异步发货（像一些道具一般属于其他服务，实体物品的话属于当前电商服务）
        ActivityDeliveryItemEntity activityDeliveryItemEntity = new ActivityDeliveryItemEntity();
        activityDeliveryItemEntity.setUserId(drawEntity.getUserId());
        activityDeliveryItemEntity.setItemId(activitySetEntity.getItemId());
        messageProducer.publishActivityDeliveryItemMessage(activityDeliveryItemEntity);

        //7（不在这里实现）还可以加定时任务在活动结束时再查询一下真实送出的物品数量更新到数据库（因为上面第5步的数据可能是不准确的）
        ActivityDrawResultVO activityDrawResultVO = new ActivityDrawResultVO();
        activityDrawResultVO.setUserId(drawEntity.getUserId());
        activityDrawResultVO.setItemId(activitySetEntity.getItemId());
        return activityDrawResultVO;
    }

    private ActivitySetEntity doDrawBefore(ActivityDrawEntity drawEntity) {
        //  活动时间
        ActivitySetEntity activitySetting = activityRepository.getActivitySetting(drawEntity.getActivityId());
        LocalDateTime current = LocalDateTime.now();
        if (current.isBefore(activitySetting.getStartTime())) {
            throw new MallDomainException(MallErrorCodes.ACTIVITY_NOT_STARTED);
        }
        if (current.isAfter(activitySetting.getEndTime())) {
            throw new MallDomainException(MallErrorCodes.ACTIVITY_ALREADY_ENDED);
        }
        //  活动物品库存
        long usedCount = activityRepository.getActivityStock(drawEntity.getActivityId());
        if (usedCount >= activitySetting.getStock()) {
            throw new MallDomainException(MallErrorCodes.ACTIVITY_STOCK_EMPTY);
        }
        //  用户参与记录
        boolean hasPartaken = activityRepository.getUserPartakeRecord(drawEntity.getActivityId(), drawEntity.getUserId());
        if (hasPartaken) {
            throw new MallDomainException(MallErrorCodes.ACTIVITY_ALREADY_PARTOOK);
        }
        return activitySetting;
    }

    @Override
    public Integer setActivity(ActivitySetEntity activitySetEntity) {
        if (!activitySetEntity.getStartTime().isBefore(activitySetEntity.getEndTime())
            || !LocalDateTime.now().isBefore(activitySetEntity.getEndTime())) {
            throw new MallDomainException(MallErrorCodes.ACTIVITY_SETTING_INVALID);
        }
        return activityRepository.setActivity(activitySetEntity);
    }

    @Override
    public void updateActivityStockAndAddActivityPartakeRecord(ActivitySyncStockEntity activitySyncStockEntity) {
        activityRepository.updateActivityStockTODB(activitySyncStockEntity);
        activityRepository.addUserPartakeRecordToDB(activitySyncStockEntity);
    }

    @Override
    public void deliveryItem(ActivityDeliveryItemEntity activityDeliveryItemEntity) {
        activityRepository.deliveryItem(activityDeliveryItemEntity);
    }
}
