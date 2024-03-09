package top.kwseeker.msa.action.user.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Sex {
    UNKNOWN(0),
    MALE(1),
    FEMALE(2);

    private final Integer sex;
}
