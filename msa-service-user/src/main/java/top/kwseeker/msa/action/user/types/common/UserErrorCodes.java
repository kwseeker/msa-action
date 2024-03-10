package top.kwseeker.msa.action.user.types.common;

import top.kwseeker.msa.action.framework.common.exception.ErrorCode;

public interface UserErrorCodes {

    // 用户服务错误段 ------------------------------------------------------
    // 用户管理 10000-19999
    ErrorCode INVALID_USERNAME_OR_PASSWORD = new ErrorCode(10000, "用户名密码错误");
    ErrorCode CREATE_USER_FAILED = new ErrorCode(10000, "用户名密码错误");
    // 用户权限管理 20000-29999

    // 认证授权 30000-39999

}
