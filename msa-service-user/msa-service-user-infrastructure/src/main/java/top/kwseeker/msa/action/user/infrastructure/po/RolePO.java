package top.kwseeker.msa.action.user.infrastructure.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import top.kwseeker.msa.action.user.infrastructure.mybatis.handler.JsonLongSetTypeHandler;
import top.kwseeker.msa.action.user.infrastructure.po.base.BasePO;

import java.util.Set;

@TableName(value = "ud_role", autoResultMap = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RolePO extends BasePO {

    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 角色标识
     * 枚举
     */
    private String code;
    /**
     * 角色排序
     */
    private Integer sort;
    /**
     * 角色状态
     */
    private Integer status;
    /**
     * 角色类型（1内置角色 2自定义角色）
     */
    private Integer type;
    /**
     * 备注
     */
    private String remark;
    /**
     * 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）
     */
    private Integer dataScope;
    /**
     * 数据范围(指定部门数组，对应 dataScope=2)
     */
    @TableField(typeHandler = JsonLongSetTypeHandler.class)
    private Set<Long> dataScopeDeptIds;
}
