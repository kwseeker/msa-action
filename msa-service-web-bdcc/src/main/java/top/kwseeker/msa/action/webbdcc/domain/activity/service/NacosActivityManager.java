package top.kwseeker.msa.action.webbdcc.domain.activity.service;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;
import top.kwseeker.msa.action.webbdcc.domain.activity.model.Activity;
import top.kwseeker.msa.action.webbdcc.domain.activity.model.ActivitySetting;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 配置通过 Nacos 更新后，需要将元配置信息转成 Activity 对象缓存到本地
 * ActivitySettingProperties 配置变更后， Spring Cloud 会发布 EnvironmentChangeEvent 事件，
 * 监听此事件将配置转换后缓存到本地
 * 另外也可以仿造 RefreshEventListener 实现监听
 */
@Slf4j
@Component
public class NacosActivityManager implements ApplicationListener<EnvironmentChangeEvent> {

    private static final String ACTIVITY_PREFIX = "msa.activities";

    private final ActivitySettingProperties activitySettingProperties;
    private final ConfigurableEnvironment environment;

    private Map<String, Activity> activityMap = new HashMap<>();
    private Map<String, Activity> enabledActivityMap = new HashMap<>();

    public NacosActivityManager(ActivitySettingProperties activitySettingProperties,
                                ConfigurableEnvironment environment) {
        this.activitySettingProperties = activitySettingProperties;
        this.environment = environment;
    }

    @PostConstruct
    public void init() {
        reloadFromProperties();
    }

    @Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {
        boolean needReload = false;
        for (String key : event.getKeys()) {
            if (!key.startsWith(ACTIVITY_PREFIX)) {
                continue;
            }
            log.info("活动配置更新，key：{} value：{}", key, environment.getProperty(key));
            needReload = true;
        }
        if (needReload) {
            reloadFromProperties();
        }
    }

    private void reloadFromProperties() {
        Map<String, ActivitySetting> activities = activitySettingProperties.getActivities();
        if (activities == null || activities.isEmpty()) {
            activityMap = Collections.emptyMap();
            enabledActivityMap = Collections.emptyMap();
            return;
        }

        Map<String, Activity> newActivityMap = new HashMap<>();
        Map<String, Activity> newEnabledActivityMap = new HashMap<>();
        for (Map.Entry<String, ActivitySetting> entry : activities.entrySet()) {
            String activityId = entry.getKey();
            ActivitySetting activitySetting = entry.getValue();
            try {
                Class<? extends Activity> clazz = (Class<? extends Activity>) Class.forName(activitySetting.getActivityClassName());
                Activity activity = JSON.parseObject(activitySetting.getActivityJson(), clazz);
                newActivityMap.put(activityId, activity);
                if (activitySetting.isEnable()) {
                    newEnabledActivityMap.put(activityId, activity);
                }
            } catch (ClassNotFoundException e) {
                log.error("活动配置中存在无效的活动类型，活动Id:{}, Class:{}", activityId, activitySetting.getActivityClassName());
            }
        }
        activityMap = Collections.unmodifiableMap(newActivityMap);
        enabledActivityMap = Collections.unmodifiableMap(newEnabledActivityMap);
    }

    public List<Activity> getEnabledActivities() {
        return new ArrayList<>(enabledActivityMap.values());
    }
}
