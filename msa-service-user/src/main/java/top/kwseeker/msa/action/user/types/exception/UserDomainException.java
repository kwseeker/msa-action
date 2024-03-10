package top.kwseeker.msa.action.user.types.exception;

import top.kwseeker.msa.action.framework.common.exception.ErrorCode;

public class UserDomainException extends RuntimeException {

    private final Integer code;
    private String message;

    public UserDomainException(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public UserDomainException(ErrorCode errorCode, Throwable cause) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        super.initCause(cause);
    }

    public UserDomainException(Integer code) {
        this.code = code;
    }

    public UserDomainException(Integer code, Throwable cause) {
        this.code = code;
        super.initCause(cause);
    }

    public UserDomainException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public UserDomainException(Integer code, String message, Throwable cause) {
        this.code = code;
        this.message = message;
        super.initCause(cause);
    }
}