package top.kwseeker.msa.action.sentinel.slots.block.user;

import com.alibaba.csp.sentinel.slots.block.BlockException;

public class UserAuthorityException extends BlockException {

    private String limitUser;

    public UserAuthorityException(String limitUser, String limitApp) {
        super(limitApp);
        this.limitUser = limitUser;
    }

    public UserAuthorityException(String limitUser, String limitApp, UserAuthorityRule rule) {
        super(limitApp, rule);
        this.limitUser = limitUser;
    }

    public UserAuthorityException(String limitUser, String message, Throwable cause) {
        super(message, cause);
        this.limitUser = limitUser;
    }

    public UserAuthorityException(String message) {
        super(message, (Throwable) null);
    }

    public UserAuthorityException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAuthorityException(String limitUser, String limitApp, String message) {
        super(limitApp, message);
        this.limitUser = limitUser;
    }

    @Override
    public UserAuthorityRule getRule() {
        return rule.as(UserAuthorityRule.class);
    }

    public String getLimitUser() {
        return limitUser;
    }
}