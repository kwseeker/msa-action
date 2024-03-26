package top.kwseeker.msa.mall.infrastructure.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户活动参与记录表
 * </p>
 *
 * @author kwseeker
 * @since 2024-03-25
 */
@TableName("md_user_activity_record")
@Data
@Builder
public class UserActivityRecordPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Integer activityId;

    private Integer count;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
