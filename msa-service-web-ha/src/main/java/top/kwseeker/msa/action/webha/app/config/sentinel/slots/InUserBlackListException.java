package top.kwseeker.msa.action.webha.app.config.sentinel.slots;

import com.alibaba.csp.sentinel.slots.block.BlockException;

public class InUserBlackListException extends BlockException {

    //public InUserBlackListException(String ruleLimitApp) {
    //    super(ruleLimitApp);
    //}

    //public InUserBlackListException(String ruleLimitApp, AuthorityRule rule) {
    //    super(ruleLimitApp, rule);
    //}

    public InUserBlackListException(String message, Throwable cause) {
        super(message, cause);
    }

    //public InUserBlackListException(String ruleLimitApp, String message) {
    //    super(ruleLimitApp, message);
    //}

    //@Override
    //public Throwable fillInStackTrace() {
    //    return this;
    //}
    //
    //@Override
    //public AuthorityRule getRule() {
    //    return rule.as(AuthorityRule.class);
    //}
}