package top.kwseeker.msa.mall.trigger.http;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kwseeker.msa.action.framework.common.model.Response;
import top.kwseeker.msa.action.security.core.util.SecurityFrameworkUtil;
import top.kwseeker.msa.mall.domain.activity.model.entity.ActivityDrawEntity;
import top.kwseeker.msa.mall.domain.activity.model.vo.ActivityDrawResultVO;
import top.kwseeker.msa.mall.domain.activity.service.IActivityService;
import top.kwseeker.msa.mall.trigger.model.converter.Converter;
import top.kwseeker.msa.mall.trigger.model.dto.ActivityDrawDTO;

import javax.annotation.Resource;

@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Resource
    private IActivityService activityService;

    /**
     * 领取物品，模拟一个限量免费送的活动（高并发场景）的处理
     */
    @PostMapping("/draw")
    public Response<ActivityDrawResultVO> drawItem(ActivityDrawDTO drawDTO) {
        Long loginUserId = SecurityFrameworkUtil.getLoginUserId();
        ActivityDrawEntity drawEntity = new ActivityDrawEntity(loginUserId, drawDTO.getActivityId());
        ActivityDrawResultVO activityDrawResultVO = activityService.drawItem(drawEntity);
        return Response.success(activityDrawResultVO);
    }
}
