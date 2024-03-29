package top.kwseeker.msa.mall.infrastructure.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.kwseeker.msa.mybatis.core.po.TimeBasePO;

/**
 * 用户活动参与记录表
 */
@TableName("md_user_activity_record")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserActivityRecordPO extends TimeBasePO {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Integer activityId;

    private Integer count;
}
