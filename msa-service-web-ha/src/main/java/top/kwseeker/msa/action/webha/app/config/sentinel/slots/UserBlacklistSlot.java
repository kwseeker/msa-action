package top.kwseeker.msa.action.webha.app.config.sentinel.slots;

import com.alibaba.csp.sentinel.context.Context;
import com.alibaba.csp.sentinel.node.DefaultNode;
import com.alibaba.csp.sentinel.slotchain.AbstractLinkedProcessorSlot;
import com.alibaba.csp.sentinel.slotchain.ResourceWrapper;
import com.alibaba.csp.sentinel.spi.Spi;

/**
 * 用户黑名单规则
 */
@Spi(order = Constants.ORDER_USER_BLACKLIST_SLOT)
public class UserBlacklistSlot extends AbstractLinkedProcessorSlot<DefaultNode> {

    @Override
    public void entry(Context context, ResourceWrapper resourceWrapper, DefaultNode node, int count, boolean prioritized, Object... args)
            throws Throwable {
        checkInUserBlacklist(resourceWrapper, context);
        fireEntry(context, resourceWrapper, node, count, prioritized, args);
    }

    @Override
    public void exit(Context context, ResourceWrapper resourceWrapper, int count, Object... args) {
        fireExit(context, resourceWrapper, count, args);
    }

    void checkInUserBlacklist(ResourceWrapper resource, Context context) throws InUserBlackListException {
        //Map<String, Set<AuthorityRule>> authorityRules = AuthorityRuleManager.getAuthorityRules();
        //if (authorityRules == null) {
        //    return;
        //}
        //Set<AuthorityRule> rules = authorityRules.get(resource.getName());
        //if (rules == null) {
        //    return;
        //}
        //
        //for (AuthorityRule rule : rules) {
        //    if (!AuthorityRuleChecker.passCheck(rule, context)) {
        //        throw new AuthorityException(context.getOrigin(), rule);
        //    }
        //}
    }
}
