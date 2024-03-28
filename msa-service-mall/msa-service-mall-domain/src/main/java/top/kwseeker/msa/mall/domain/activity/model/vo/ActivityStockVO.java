package top.kwseeker.msa.mall.domain.activity.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.kwseeker.msa.action.framework.common.exception.ErrorCode;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityStockVO {

    private ErrorCode errorCode;
    //库存 Key
    private String stockTokenKey;
    //库存剩余
    private Integer stockUsedCount;

    public ActivityStockVO(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
