package top.kwseeker.msa.action.user.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenVerifiedDTO {

    private Long userId;
    private String username;
    /**
     * 授权范围
     */
    private List<String> scopes;
}
