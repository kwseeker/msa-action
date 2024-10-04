package top.kwseeker.msa.action.basicone.domain.account.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PayResult {
    SUCCESS(0),
    FAILED(1);

    private final Integer code;
}
