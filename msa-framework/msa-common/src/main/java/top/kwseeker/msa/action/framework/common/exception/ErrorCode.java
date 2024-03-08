package top.kwseeker.msa.action.framework.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorCode {

    //错误码
    private final Integer code;
    //错误信息
    private final String message;

}
