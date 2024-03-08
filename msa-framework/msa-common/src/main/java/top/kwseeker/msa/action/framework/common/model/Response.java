package top.kwseeker.msa.action.framework.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.kwseeker.msa.action.framework.common.exception.ErrorCode;
import top.kwseeker.msa.action.framework.common.exception.GlobalErrorCodes;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> implements Serializable {

    //错误码
    private Integer code;
    //错误信息
    private String message;
    //返回数据
    private T data;

    public static <T> Response<T> success(T data) {
        Response<T> response = new Response<>();
        response.code = GlobalErrorCodes.SUCCESS.getCode();
        response.message = GlobalErrorCodes.SUCCESS.getMessage();
        response.data = data;
        return response;
    }

    public static <T> Response<T> fail(ErrorCode errorCode) {
        Response<T> response = new Response<>();
        response.code = errorCode.getCode();
        response.message = errorCode.getMessage();
        return response;
    }
}
