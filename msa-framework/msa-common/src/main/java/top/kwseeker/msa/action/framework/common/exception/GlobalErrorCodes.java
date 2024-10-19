package top.kwseeker.msa.action.framework.common.exception;

public interface GlobalErrorCodes {

    ErrorCode SUCCESS = new ErrorCode(0, "成功");

    // ========== 客户端错误 ==========

    ErrorCode BAD_REQUEST = new ErrorCode(400, "请求参数不正确");
    ErrorCode UNAUTHORIZED = new ErrorCode(401, "账号未登录");
    ErrorCode FORBIDDEN = new ErrorCode(403, "没有该操作权限");

    // ========== 服务端错误 ==========

    ErrorCode INTERNAL_SERVER_ERROR = new ErrorCode(500, "系统异常");

    ErrorCode REQUEST_DEGRADE_ERROR = new ErrorCode(601, "请求降级");
    ErrorCode FLOW_EXCEED_ERROR = new ErrorCode(602, "系统流量异常，请稍后重试");
}

