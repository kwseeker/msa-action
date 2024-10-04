package top.kwseeker.msa.action.basicone.domain.account.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Payment {

    private String userId;
    private String orderId;
    private Integer amount;
}
