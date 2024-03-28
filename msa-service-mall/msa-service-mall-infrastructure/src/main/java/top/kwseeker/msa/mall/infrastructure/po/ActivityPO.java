package top.kwseeker.msa.mall.infrastructure.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.kwseeker.msa.mybatis.core.po.BasePO;

import java.time.LocalDateTime;

/**
 * 活动表
 */
@TableName("md_activity")
@Data
@EqualsAndHashCode(callSuper = true)
public class ActivityPO extends BasePO {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private Integer settingId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
