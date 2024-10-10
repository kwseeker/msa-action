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
    private ActivityManager activityManager;

    @Override
    public void addActivity(ActivitySettingDTO activitySettingDTO) throws Exception {
        Activity activity = activitySettingDTO.getActivity();
        ActivitySetting setting = new ActivitySetting();
        setting.setEnabled(activitySettingDTO.getEnable() == ActivityConstants.ENABLED);
        setting.setActivityJson(JSON.toJSONString(activity));
        setting.setActivityClassName(activity.getClass().getName());
        activityManager.setActivity(activity.getId(), setting);
    }

    @Override
    public void deleteActivity(String activityId) throws Exception {
        activityManager.deleteActivity(activityId);
    }

    @Override
    public void enableActivity(String activityId) throws Exception {
        ActivitySetting setting = activityManager.getActivity(activityId);
        setting.setEnabled(true);
        activityManager.setActivity(activityId, setting);
    }

    @Override
    public List<Activity> enabledActivities() {
        return activityManager.getEnabledActivities();
    }
}
