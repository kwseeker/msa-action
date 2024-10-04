package top.kwseeker.msa.action.basicone.domain.account.service;

import top.kwseeker.msa.action.basicone.domain.account.model.PayResult;
import top.kwseeker.msa.action.basicone.domain.account.model.Payment;

public interface AccountService {

    PayResult pay(Payment payment);
}
