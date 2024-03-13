package top.kwseeker.msa.action.user.domain.auth.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.kwseeker.msa.action.user.types.enums.Sex;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateVO {

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
}