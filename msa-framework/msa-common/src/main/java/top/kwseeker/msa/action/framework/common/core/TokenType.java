package top.kwseeker.msa.action.framework.common.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenType {
    JWT(1),
    OAuth2(2)
    ;

    private final int type;
}
