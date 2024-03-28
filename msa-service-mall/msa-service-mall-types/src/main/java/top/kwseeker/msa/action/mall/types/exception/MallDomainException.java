package top.kwseeker.msa.action.mall.types.exception;

import top.kwseeker.msa.action.framework.common.exception.ErrorCode;

public class MallDomainException extends RuntimeException {

    private final Integer code;
    private String message;

    public MallDomainException(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public MallDomainException(ErrorCode errorCode, Throwable cause) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        super.initCause(cause);
    }

    public MallDomainException(Integer code) {
        this.code = code;
    }

    public MallDomainException(Integer code, Throwable cause) {
        this.code = code;
        super.initCause(cause);
    }

    public MallDomainException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public MallDomainException(Integer code, String message, Throwable cause) {
        this.code = code;
        this.message = message;
        super.initCause(cause);
    }
}