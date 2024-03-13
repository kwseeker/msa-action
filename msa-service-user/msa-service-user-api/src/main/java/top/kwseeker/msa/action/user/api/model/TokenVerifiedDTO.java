package top.kwseeker.msa.action.user.api.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TokenVerifiedDTO {

    private Long userId;
    /**
     * 授权范围
     */
    private List<String> scopes;
}
