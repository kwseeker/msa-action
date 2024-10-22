package top.kwseeker.msa.action.sentinel.slots.block.user;

import com.alibaba.csp.sentinel.slots.block.AbstractRule;

public class UserAuthorityRule extends AbstractRule {

    // 默认实现黑名单
    private int strategy = UserAuthorityConstants.USER_AUTHORITY_BLACK;
    // 被限制的用户ID列表
    private String limitUsers;
    // 如果用户ID很多，可以使用字典树优化
    //private Node dict;

    public UserAuthorityRule() {
        setResource(UserAuthorityConstants.RESOURCE_ALL);
    }

    public UserAuthorityRule(int strategy, String limitUsers) {
        this.strategy = strategy;
        this.limitUsers = limitUsers;
        setResource(UserAuthorityConstants.RESOURCE_ALL);
    }

    public int getStrategy() {
        return strategy;
    }

    public void setStrategy(int strategy) {
        this.strategy = strategy;
    }

    public String getLimitUsers() {
        return limitUsers;
    }

    public void setLimitUsers(String limitUsers) {
        this.limitUsers = limitUsers;
    }
}
