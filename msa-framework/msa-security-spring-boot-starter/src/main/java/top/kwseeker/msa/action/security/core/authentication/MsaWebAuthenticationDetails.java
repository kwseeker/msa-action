package top.kwseeker.msa.action.security.core.authentication;

import lombok.Getter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

@Getter
public class MsaWebAuthenticationDetails extends WebAuthenticationDetails {

    private final String uri;

    public MsaWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        this.uri = request.getRequestURI();
    }
}
