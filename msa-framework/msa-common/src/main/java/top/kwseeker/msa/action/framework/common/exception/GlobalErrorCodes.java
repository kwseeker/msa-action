package top.kwseeker.msa.action.framework.common.exception;

public interface GlobalErrorCodes {

    ErrorCode SUCCESS = new ErrorCode(0, "成功");

    // ========== 客户端错误 ==========

    ErrorCode BAD_REQUEST = new ErrorCode(1000, "请求参数不正确");
    ErrorCode UNAUTHORIZED = new ErrorCode(1001, "账号未登录");
    ErrorCode FORBIDDEN = new ErrorCode(1002, "没有该操作权限");
}
