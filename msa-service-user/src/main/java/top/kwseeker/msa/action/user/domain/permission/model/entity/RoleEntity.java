package top.kwseeker.msa.action.user.domain.permission.model.entity;

import lombok.Data;

import java.util.Set;

@Data
public class RoleEntity {

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
    private Set<Long> dataScopeDeptIds;
}
