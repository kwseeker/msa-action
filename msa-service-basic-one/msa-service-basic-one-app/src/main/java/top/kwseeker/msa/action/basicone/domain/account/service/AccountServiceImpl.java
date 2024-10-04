package top.kwseeker.msa.action.basicone.domain.account.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.kwseeker.msa.action.basicone.domain.account.model.PayResult;
import top.kwseeker.msa.action.basicone.domain.account.model.Payment;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    @Override
    public PayResult pay(Payment payment) {
        log.debug("payment: {}", payment);
        return PayResult.SUCCESS;
    }
}
