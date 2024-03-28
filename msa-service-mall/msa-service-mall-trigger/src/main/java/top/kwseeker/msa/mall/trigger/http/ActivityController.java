package top.kwseeker.msa.mall.trigger.http;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kwseeker.msa.action.framework.common.exception.GlobalErrorCodes;
import top.kwseeker.msa.action.framework.common.model.Response;
import top.kwseeker.msa.action.security.core.LoginUser;
import top.kwseeker.msa.action.security.core.util.SecurityFrameworkUtil;
import top.kwseeker.msa.mall.domain.activity.model.entity.ActivityDrawEntity;
import top.kwseeker.msa.mall.domain.activity.model.entity.ActivitySetEntity;
import top.kwseeker.msa.mall.domain.activity.model.vo.ActivityDrawResultVO;
import top.kwseeker.msa.mall.domain.activity.service.IActivityService;
import top.kwseeker.msa.mall.trigger.model.dto.ActivityDrawDTO;
import top.kwseeker.msa.mall.trigger.model.dto.ActivitySetDTO;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;

@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Resource
    private IActivityService activityService;

    /**
     * 领取物品，模拟一个限量免费送的活动（高并发场景）的处理
     */
    @PermitAll
    @PostMapping("/draw")
    public Response<ActivityDrawResultVO> drawItem(ActivityDrawDTO drawDTO) {
        Long loginUserId = SecurityFrameworkUtil.getLoginUserId();
        ActivityDrawEntity drawEntity = new ActivityDrawEntity(loginUserId, drawDTO.getActivityId());
        ActivityDrawResultVO activityDrawResultVO = activityService.drawItem(drawEntity);
        return Response.success(activityDrawResultVO);
    }

    /**
     * 活动设置（运营接口，后面移到运营后台服务）
     */
    @PreAuthorize("@pv.verifyPerms('biz:activtiy:create')")
    @PutMapping("/set")
    public Response<Integer> setActivity(ActivitySetDTO activitySetDTO) {
        LoginUser loginUser = SecurityFrameworkUtil.getLoginUser();
        if (loginUser == null) {
            return Response.fail(GlobalErrorCodes.UNAUTHORIZED);
        }

        ActivitySetEntity activitySetEntity = ActivitySetEntity.builder()
                .itemId(activitySetDTO.getItemId())
                .stock(activitySetDTO.getStock())
                .creator(loginUser.getUsername())
                .build();
        Integer activityId = activityService.setActivity(activitySetEntity);
        return Response.success(activityId);
    }
}
