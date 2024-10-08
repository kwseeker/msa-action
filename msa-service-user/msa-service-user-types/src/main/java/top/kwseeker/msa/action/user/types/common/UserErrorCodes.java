package top.kwseeker.msa.action.user.types.common;

import top.kwseeker.msa.action.framework.common.exception.ErrorCode;

public interface UserErrorCodes {

    // 用户服务错误段 ------------------------------------------------------
    // 用户管理 10000-10999
    ErrorCode INVALID_USERNAME_OR_PASSWORD = new ErrorCode(10000, "用户名密码错误");
    ErrorCode CREATE_USER_FAILED = new ErrorCode(10001, "用户名密码错误");
    // 用户权限管理 11000-11999

    // 认证授权 12000-12999
    ErrorCode CREATE_TOKEN_FAILED = new ErrorCode(12000, "生成TOKEN失败");
    ErrorCode VERIFY_TOKEN_FAILED = new ErrorCode(12001, "校验TOKEN失败");

}
