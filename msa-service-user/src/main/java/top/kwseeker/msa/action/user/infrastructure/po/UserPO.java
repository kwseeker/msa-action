package top.kwseeker.msa.action.user.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import top.kwseeker.msa.action.user.infrastructure.po.base.BasePO;
import top.kwseeker.msa.action.user.types.enums.Sex;

import java.time.LocalDateTime;

@TableName(value = "ud_user")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserPO extends BasePO {

    @TableId
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String remark;
    /**
     * 部门ID
     */
    private Long deptId;
    private String email;
    private String mobile;
    /**
     * 用户性别枚举
     * {@link Sex}
     */
    private Integer sex;
    private String avatar;
    private Integer status;
    private String loginIp;
    private LocalDateTime loginDate;
}
