package top.kwseeker.msa.action.user.infrastructure.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.kwseeker.msa.action.user.infrastructure.po.base.BasePO;

@TableName("ud_user_role")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserRolePO extends BasePO {

    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 用户 ID
     */
    private Long userId;
    /**
     * 角色 ID
     */
    private Long roleId;

}
