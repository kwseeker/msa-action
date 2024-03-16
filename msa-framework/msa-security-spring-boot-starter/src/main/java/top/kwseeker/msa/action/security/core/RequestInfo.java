package top.kwseeker.msa.action.security.core;

import lombok.Builder;
import lombok.Data;
import top.kwseeker.msa.action.security.core.authentication.MsaWebAuthenticationDetails;

@Data
@Builder
public class RequestInfo {

    private LoginUser loginUser;
    private MsaWebAuthenticationDetails requestDetails;
}
