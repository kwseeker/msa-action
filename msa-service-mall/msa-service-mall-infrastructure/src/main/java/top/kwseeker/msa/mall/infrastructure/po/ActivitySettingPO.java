package top.kwseeker.msa.mall.infrastructure.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import top.kwseeker.msa.mybatis.core.po.BasePO;

/**
 * 活动配置表
 */
@TableName("md_activity_setting")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ActivitySettingPO extends BasePO {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer itemId;

    private Integer stock;

    private Integer usedCount;
}
