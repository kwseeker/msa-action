package top.kwseeker.msa.mall.infrastructure.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.kwseeker.msa.mybatis.core.po.TimeBasePO;

/**
 * 用户物品表
 */
@TableName("md_user_item")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserItemPO extends TimeBasePO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Integer itemId;
}
