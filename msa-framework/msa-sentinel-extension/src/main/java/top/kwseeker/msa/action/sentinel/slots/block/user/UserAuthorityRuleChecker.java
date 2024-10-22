package top.kwseeker.msa.action.sentinel.slots.block.user;

import com.alibaba.csp.sentinel.util.StringUtil;
import top.kwseeker.msa.action.framework.common.context.UserContext;

final class UserAuthorityRuleChecker {

    static boolean passCheck(UserAuthorityRule rule, UserContext context) {
        String userId = context.getUserId();
        if (StringUtil.isEmpty(userId)) {
            return false;
        }
        int strategy = rule.getStrategy();
        if (StringUtil.isEmpty(rule.getLimitUsers())) {
            return strategy == UserAuthorityConstants.USER_AUTHORITY_BLACK;
        }

        int pos = rule.getLimitUsers().indexOf(userId);
        boolean contain = pos > -1;
        if (contain) {
            boolean exactlyMatch = false;
            String[] userArray = rule.getLimitUsers().split(",");
            for (String user : userArray) {
                if (userId.equals(user.trim())) {
                    exactlyMatch = true;
                    break;
                }
            }
            contain = exactlyMatch;
        }
        if ((strategy == UserAuthorityConstants.USER_AUTHORITY_BLACK && contain)
            || (strategy == UserAuthorityConstants.USER_AUTHORITY_WHITE && !contain)) {
            return false;
        }

        return true;
    }
}
