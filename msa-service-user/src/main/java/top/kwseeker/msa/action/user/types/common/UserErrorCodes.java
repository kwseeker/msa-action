package top.kwseeker.msa.action.user.types.common;

import top.kwseeker.msa.action.framework.common.exception.ErrorCode;

public interface UserErrorCodes {

    // 用户服务错误段
    ErrorCode INVALID_USERNAME_OR_PASSWORD = new ErrorCode(10000, "用户名密码错误");
}
