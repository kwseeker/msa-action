package top.kwseeker.msa.action.mall.types.exception;

import top.kwseeker.msa.action.framework.common.exception.ErrorCode;

public interface MallErrorCodes {

    // 电商服务错误段 ------------------------------------------------------
    // 活动管理 20000-20999
    ErrorCode ACTIVITY_CREATE_FAILED = new ErrorCode(20000, "创建活动失败");
    ErrorCode ACTIVITY_SETTING_INVALID = new ErrorCode(20001, "活动配置无效");
    ErrorCode ACTIVITY_NOT_STARTED = new ErrorCode(20002, "活动尚未开始");
    ErrorCode ACTIVITY_ALREADY_ENDED = new ErrorCode(20003, "活动尚未已经结束");
    ErrorCode ACTIVITY_STOCK_EMPTY = new ErrorCode(20004, "活动商品已派发完毕");
    ErrorCode ACTIVITY_ALREADY_PARTOOK = new ErrorCode(20005, "活动已参与");
    ErrorCode ACTIVITY_OUT_OF_STOCK = new ErrorCode(20006, "活动无库存");
    ErrorCode ACTIVITY_TRY_LOCK_FAILED = new ErrorCode(20006, "活动竞争锁失败");
}
