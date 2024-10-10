package top.kwseeker.msa.action.webbdcc.domain.activity.service;

import top.kwseeker.msa.action.webbdcc.api.dto.ActivitySettingDTO;
import top.kwseeker.msa.action.webbdcc.domain.activity.model.Activity;

import java.util.List;

public interface IActivityService {

    void addActivity(ActivitySettingDTO activitySetting) throws Exception;

    void deleteActivity(String activityId) throws Exception;

    void enableActivity(String activityId) throws Exception;

    List<Activity> enabledActivities();
}
