package top.kwseeker.msa.action.user.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.kwseeker.msa.action.user.infrastructure.po.base.BasePO;

@TableName("ud_role_menu")
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleMenuPO extends BasePO {

    /**
     * 自增主键
     */
    @TableId
    private Long id;
    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * 菜单ID
     */
    private Long menuId;

}