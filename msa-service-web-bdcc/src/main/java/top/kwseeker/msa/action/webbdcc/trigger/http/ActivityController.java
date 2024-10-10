package top.kwseeker.msa.action.webbdcc.trigger.http;

import org.springframework.web.bind.annotation.*;
import top.kwseeker.msa.action.framework.common.model.Response;
import top.kwseeker.msa.action.webbdcc.api.dto.ActivitySettingDTO;
import top.kwseeker.msa.action.webbdcc.domain.activity.model.Activity;
import top.kwseeker.msa.action.webbdcc.domain.activity.service.IActivityService;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Resource
    private IActivityService activityService;

    /**
     * 不存在则新增，存在则更新
     */
    @PostMapping
    public Response<Void> addActivity(@RequestBody ActivitySettingDTO request) throws Exception {
        activityService.addActivity(request);
        return Response.success(null);
    }

    @DeleteMapping
    public Response<Void> deleteActivity(@PathParam("actId") String actId) throws Exception {
        activityService.deleteActivity(actId);
        return Response.success(null);
    }

    @PostMapping("/enable")
    public Response<Void> enableActivity(@PathParam("actId") String actId) throws Exception {
        activityService.enableActivity(actId);
        return Response.success(null);
    }

    @GetMapping("/enabled")
    public Response<List<Activity>> enabledActivities() throws Exception {
        List<Activity> activities = activityService.enabledActivities();
        return Response.success(activities);
    }
}
