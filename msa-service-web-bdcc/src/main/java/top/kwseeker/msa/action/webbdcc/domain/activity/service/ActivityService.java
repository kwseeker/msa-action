package top.kwseeker.msa.action.webbdcc.domain.activity.service;

import com.alibaba.fastjson2.JSON;
import org.springframework.stereotype.Service;
import top.kwseeker.msa.action.webbdcc.api.dto.ActivitySettingDTO;
import top.kwseeker.msa.action.webbdcc.domain.activity.model.Activity;
import top.kwseeker.msa.action.webbdcc.domain.activity.model.ActivityConstants;
import top.kwseeker.msa.action.webbdcc.domain.activity.model.ActivitySetting;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ActivityService implements IActivityService {

    @Resource
    private ZkActivityManager zkActivityManager;

    @Override
    public void addActivity(ActivitySettingDTO activitySettingDTO) throws Exception {
        Activity activity = activitySettingDTO.getActivity();
        ActivitySetting setting = new ActivitySetting();
        setting.setEnable(activitySettingDTO.getEnable() == ActivityConstants.ENABLED);
        setting.setActivityJson(JSON.toJSONString(activity));
        setting.setActivityClassName(activity.getClass().getName());
        zkActivityManager.setActivityNode(activity.getId(), setting);
    }

    @Override
    public void deleteActivity(String activityId) throws Exception {
        zkActivityManager.deleteActivityNode(activityId);
    }

    @Override
    public void enableActivity(String activityId) throws Exception {
        ActivitySetting setting = zkActivityManager.getActivityNode(activityId);
        setting.setEnable(true);
        zkActivityManager.setActivityNode(activityId, setting);
    }

    @Override
    public List<Activity> enabledActivities() {
        return zkActivityManager.getEnabledActivities();
    }
}
