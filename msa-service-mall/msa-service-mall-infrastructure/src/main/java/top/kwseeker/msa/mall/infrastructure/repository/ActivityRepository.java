package top.kwseeker.msa.mall.infrastructure.repository;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.stereotype.Repository;
import top.kwseeker.msa.action.framework.common.exception.GlobalErrorCodes;
import top.kwseeker.msa.action.mall.types.exception.MallDomainException;
import top.kwseeker.msa.action.mall.types.exception.MallErrorCodes;
import top.kwseeker.msa.mall.domain.activity.model.entity.ActivitySetEntity;
import top.kwseeker.msa.mall.domain.activity.model.vo.ActivityStockVO;
import top.kwseeker.msa.mall.domain.activity.repository.IActivityRepository;
import top.kwseeker.msa.mall.infrastructure.dao.ActivityMapper;
import top.kwseeker.msa.mall.infrastructure.dao.ActivitySettingMapper;
import top.kwseeker.msa.mall.infrastructure.po.ActivityPO;
import top.kwseeker.msa.mall.infrastructure.po.ActivitySettingPO;
import top.kwseeker.msa.mall.infrastructure.redis.IRedisService;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
public class ActivityRepository  implements IActivityRepository {

    public static final String ActivityCacheKeyPrefix = "msa:mall:activity:";
    public static final String SettingKeyPrefix = ActivityCacheKeyPrefix + "setting:";
    public static final String StockKeyPrefix = ActivityCacheKeyPrefix + "stock:";
    public static final String StockBindPrefix = ActivityCacheKeyPrefix + "stockBind:";
    public static final String UserPartakeRecordKeyPrefix = ActivityCacheKeyPrefix + "partakeUsers:";
    public static final long OneDayMillis = 24 * 3600 * 1000;

    @Resource
    private ActivityMapper activityMapper;
    @Resource
    private ActivitySettingMapper activitySettingMapper;
    @Resource
    private IRedisService redisService;

    @Override
    public Integer setActivity(ActivitySetEntity activitySetEntity) {
        ActivitySettingPO activitySettingPO = new ActivitySettingPO();
        activitySettingPO.setItemId(activitySetEntity.getItemId());
        activitySettingPO.setStock(activitySetEntity.getStock());
        activitySettingPO.setCreator(activitySetEntity.getCreator());
        boolean insertRet = activitySettingMapper.save(activitySettingPO);
        if (!insertRet) {
            throw new MallDomainException(MallErrorCodes.ACTIVITY_CREATE_FAILED);
        }
        ActivityPO activityPO = new ActivityPO();
        activityPO.setCreator(activitySetEntity.getCreator());
        activityPO.setName(activitySetEntity.getName());
        activityPO.setStartTime(activitySetEntity.getStartTime());
        activityPO.setEndTime(activitySetEntity.getEndTime());
        insertRet = activityMapper.save(activityPO);
        if (!insertRet) {
            throw new MallDomainException(MallErrorCodes.ACTIVITY_CREATE_FAILED);
        }

        activitySetEntity.setActivityId(activityPO.getId());
        //缓存预热，活动开始前提前加载到内存
        long ttl = ChronoUnit.MILLIS.between(activitySetEntity.getEndTime(), LocalDateTime.now());
        redisService.setValue(SettingKeyPrefix + activitySetEntity.getActivityId(), activitySetEntity, ttl);

        return activityPO.getId();
    }

    @Override
    public ActivitySetEntity getActivitySetting(Integer activityId) {
        ActivitySetEntity activitySetEntity = redisService.getValue(SettingKeyPrefix + activityId);
        if (activitySetEntity != null) {
            return activitySetEntity;
        }
        //从数据库中查询(防止万一缓存被误删，Redis断电、缓存预热时设置超时时间不对)
        //先加一把自旋锁，这个锁是为了防止发生缓存雪崩，只有获取到锁的线程才会去查数据库
        //Redisson默认的自旋锁，会不断重试获取锁，初始等待1ms,后面每次+2ms,最高等待128ms,直到获取锁、或者超时（ttl）
        RLock lock = redisService.getSpinLock(SettingKeyPrefix + activityId);
        try {
            lock.lock(200, TimeUnit.MILLISECONDS);
            //走到这里有两种可能：1）获取到锁 2）超时
            //纵使获取到锁，也先再查一下redis，可能其他线程已经更新了缓存，不需要再查数据库了
            activitySetEntity = redisService.getValue(SettingKeyPrefix + activityId);
            if (activitySetEntity != null) {
                return activitySetEntity;
            }
            //查数据库
            ActivityPO activityPO = activityMapper.selectOne(ActivityPO::getId, activityId);
            ActivitySettingPO activitySettingPO = activitySettingMapper.selectOne(ActivitySettingPO::getId, activityPO.getSettingId());
            return ActivitySetEntity.builder()
                    .activityId(activityPO.getId())
                    .name(activityPO.getName())
                    .itemId(activitySettingPO.getItemId())
                    .stock(activitySettingPO.getStock())
                    .startTime(activityPO.getStartTime())
                    .endTime(activityPO.getEndTime())
                    .build();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 注意这里返回已派发数量
     */
    @Override
    public Integer getActivityStock(Integer activityId) {
        Integer value = redisService.getValue(StockKeyPrefix + activityId);
        return value == null ? 0 : value;
    }



    @Override
    public boolean getUserPartakeRecord(Integer activityId, Long userId) {
        return redisService.isSetMember(UserPartakeRecordKeyPrefix + activityId, userId.toString());
    }

    @Override
    public void addUserPartakeRecord(Integer activityId, Long userId) {
        redisService.addToSet(UserPartakeRecordKeyPrefix + activityId, userId.toString());
    }

    @Override
    public ActivityStockVO subtractActivityStock(ActivitySetEntity activitySetEntity, Long userId) {
        Integer activityId = activitySetEntity.getActivityId();
        String activityStockKey = StockKeyPrefix + activityId;
        //注意 incr decr 命令本身是线程安全的，但是方法返回值不是线程安全的，stockUsedCount可能比当前实际的已派发数量要小
        //所以还需要额外使用分布式锁防止超卖
        long stockUsedCount = redisService.incr(activityStockKey);
        if (stockUsedCount > activitySetEntity.getStock()) {  //超出肯定是已经送完了,未超出则不一定没送完
            redisService.decr(activityStockKey);
            //已送完，秒杀失败
            return new ActivityStockVO(MallErrorCodes.ACTIVITY_STOCK_EMPTY);
        }

        //走到这有两种情况：1）实际已经送完了(比如因为并发实际已经发完了，但是stockUsedCount无法返回最新的值) 2）没送完
        //这个锁可以防止因为情况1引起的超卖问题
        String stockTokenKey = StockBindPrefix + activityId + "_" + stockUsedCount;
        long ttlMillis =  + 2 * OneDayMillis;   //加一天时间，没有必要着急删除，可以根据这个和发货进行对账
        boolean lockToken = redisService.setIfAbsent(stockTokenKey, userId, ttlMillis);
        if (!lockToken) {
            //抢锁失败，秒杀失败
            log.info("免费送活动:活动Id:{}, 用户:{}秒杀失败（竞争锁失败）, stockTokenKey={}", activityId, userId, stockTokenKey);
            return new ActivityStockVO(MallErrorCodes.ACTIVITY_TRY_LOCK_FAILED);
        }

        return new ActivityStockVO(GlobalErrorCodes.SUCCESS, stockTokenKey, (int) stockUsedCount);
    }
}
