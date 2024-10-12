package top.kwseeker.msa.action.webbdcc.domain.activity.service;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Component;
import top.kwseeker.msa.action.webbdcc.domain.activity.model.Activity;
import top.kwseeker.msa.action.webbdcc.domain.activity.model.ActivitySetting;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class ZkActivityManager {

    private static final String ZK_BDCC_CONFIG_NODE_PATH = "/msa/bdcc/configs";
    // activities/
    //   settings/                  每个活动设置一个节点更好些，方便配置修改，且活动间配置隔离避免修改活动时误改了其他活动配置
    //     MID_AUTUMN_FESTIVAL/     ActivitySetting JSON格式配置
    private static final String ACTIVITY_CONFIG_NODE_PATH = ZK_BDCC_CONFIG_NODE_PATH + "/activities";
    private static final String ACTIVITY_SETTINGS_NODE_PATH = ACTIVITY_CONFIG_NODE_PATH + "/settings";
    // 节点第一次设置值的时候，监听器不会监听到，所以创建节点时需要先设置一个空字符串，后面再配置值后都可以被监听到

    private final Map<String, Activity> activityMap = new ConcurrentHashMap<>();
    private final Map<String, Activity> enabledActivityMap = new ConcurrentHashMap<>();
    private final CuratorFramework zkClient;

    public ZkActivityManager(CuratorFramework zkClient) {
        this.zkClient = zkClient;
    }

    @PostConstruct
    public void init() throws Exception {
        checkOrCreateNode(ACTIVITY_SETTINGS_NODE_PATH);
        initializeActivityMap();
        registerCacheWatcher();
    }

    /**
     * 检查活动配置节点是否存在不存在则创建
     */
    private void checkOrCreateNode(String nodePath) throws Exception {
        Stat stat = zkClient.checkExists().forPath(nodePath);
        if (stat == null) {
            zkClient.create().creatingParentsIfNeeded().forPath(nodePath);
            log.info("Zookeeper BDCC config node: {} not exit and create", nodePath);
        }
    }

    /**
     * 获取ACTIVITY_SETTINGS_NODE_PATH下所有子节点，解析ActivitySetting，更新activityMap
     */
    private void initializeActivityMap() throws Exception {
        zkClient.getChildren().forPath(ACTIVITY_SETTINGS_NODE_PATH).forEach(activityNodePath -> {
            try {
                refreshActivityMapFromRemote(activityNodePath);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void refreshActivityMapFromRemote(String activityNodePath) throws Exception {
        byte[] activitySettingBytes = zkClient.getData()
                .forPath(ZKPaths.makePath(ACTIVITY_SETTINGS_NODE_PATH, activityNodePath));
        refreshActivityMap(activityNodePath, activitySettingBytes);
    }

    private void refreshActivityMap(String activityNodePath, byte[] activitySettingBytes) throws Exception {
        if (activitySettingBytes == null || activitySettingBytes.length == 0) {
            log.warn("ActivitySetting of {} not invalid", activityNodePath);
            activityMap.remove(activityNodePath);
            return;
        }

        ActivitySetting activitySetting = JSON.parseObject(activitySettingBytes, ActivitySetting.class);
        Class<? extends Activity> clazz = (Class<? extends Activity>) Class.forName(activitySetting.getActivityClassName());
        Activity activity = JSON.parseObject(activitySetting.getActivityJson(), clazz);
        StringBuilder sb = new StringBuilder();
        activityMap.put(activityNodePath, activity);
        sb.append(String.format("Activity zkPath: %s content: %s is loaded", activityNodePath, activity.info()));
        if (activitySetting.isEnable()) {
            enabledActivityMap.put(activityNodePath, activity);
            sb.append(" and enabled");
        } else {
            enabledActivityMap.remove(activityNodePath);
            sb.append(" and deleted");
        }
        log.info(sb.toString());
    }

    private void removeActivityMap(ChildData data) {
        String nodePath = data.getPath().substring(ACTIVITY_SETTINGS_NODE_PATH.length() + 1);
        enabledActivityMap.remove(nodePath);
        activityMap.remove(nodePath);
        log.info("Activity zkPath: {} is deleted", data.getPath());
    }

    /**
     * 注册监听，监听 settings 子节点变更
     */
    private void registerCacheWatcher() {
        CuratorCache curatorCache = CuratorCache.build(zkClient, ACTIVITY_SETTINGS_NODE_PATH);
        curatorCache.start();
        // 注册监听，内部会监听当前节点和所有子节点变化
        curatorCache.listenable().addListener((type, oldData, data) -> {    //CuratorCacheListener
            String nodePath;
            try {
                switch (type) {
                    case NODE_CREATED:  //新活动新增配置
                    case NODE_CHANGED:  //活动配置变更
                        // 忽略 settings 节点的变化
                        nodePath = data.getPath();
                        if (ACTIVITY_SETTINGS_NODE_PATH.equals(nodePath)) {
                            return;
                        }
                        String activityId = data.getPath().substring(ACTIVITY_SETTINGS_NODE_PATH.length() + 1);
                        byte[] activitySettingBytes = data.getData();
                        refreshActivityMap(activityId, activitySettingBytes);
                        break;
                    case NODE_DELETED:  //活动配置删除
                        // 忽略 settings 节点的变化
                        nodePath = oldData.getPath();
                        if (ACTIVITY_SETTINGS_NODE_PATH.equals(nodePath)) {
                            return;
                        }
                        removeActivityMap(oldData);
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 设置活动到ZK，每个活动对应一个子节点
     */
    public void setActivityNode(String activityId, ActivitySetting activitySetting) throws Exception {
        byte[] jsonBytes = JSON.toJSONBytes(activitySetting);
        checkOrCreateNode(ZKPaths.makePath(ACTIVITY_SETTINGS_NODE_PATH, activityId));
        zkClient.setData().forPath(ZKPaths.makePath(ACTIVITY_SETTINGS_NODE_PATH, activityId), jsonBytes);
    }

    public ActivitySetting getActivityNode(String activityId) throws Exception {
        byte[] bytes = zkClient.getData().forPath(ZKPaths.makePath(ACTIVITY_SETTINGS_NODE_PATH, activityId));
        return JSON.parseObject(bytes, ActivitySetting.class);
    }

    public void deleteActivityNode(String activityId) throws Exception {
        zkClient.delete().forPath(ZKPaths.makePath(ACTIVITY_SETTINGS_NODE_PATH, activityId));
    }

    public List<Activity> getEnabledActivities() {
        return new ArrayList<>(enabledActivityMap.values());
    }
}
