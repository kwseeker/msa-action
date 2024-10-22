package top.kwseeker.msa.action.sentinel.slots.block.user;

import com.alibaba.csp.sentinel.context.Context;
import com.alibaba.csp.sentinel.log.RecordLog;
import com.alibaba.csp.sentinel.node.DefaultNode;
import com.alibaba.csp.sentinel.slotchain.AbstractLinkedProcessorSlot;
import com.alibaba.csp.sentinel.slotchain.ResourceWrapper;
import com.alibaba.csp.sentinel.spi.Spi;
import com.alibaba.csp.sentinel.util.StringUtil;
import top.kwseeker.msa.action.framework.common.context.UserContext;
import top.kwseeker.msa.action.sentinel.context.UserContextUtil;

import java.util.Map;
import java.util.Set;

/**
 * 用户黑名单规则
 */
@Spi(order = UserAuthorityConstants.ORDER_USER_AUTHORITY_SLOT)   //放在 AuthoritySlot 之后
public class UserAuthoritySlot extends AbstractLinkedProcessorSlot<DefaultNode> {

    public UserAuthoritySlot() {
        RecordLog.info("[UserAuthoritySlot] loaded by SPI");
    }

    @Override
    public void entry(Context context, ResourceWrapper resourceWrapper, DefaultNode node, int count, boolean prioritized, Object... args)
            throws Throwable {
        checkUserAuthority(resourceWrapper, context);
        fireEntry(context, resourceWrapper, node, count, prioritized, args);
    }

    @Override
    public void exit(Context context, ResourceWrapper resourceWrapper, int count, Object... args) {
        fireExit(context, resourceWrapper, count, args);
    }

    /**
     * 校验用户黑白名单
     * 规则中资源名称为空则是针对所有资源，否则针对指定资源
     */
    void checkUserAuthority(ResourceWrapper resource, Context context) throws UserAuthorityException {
        //资源名 -> 对应的用户黑白名单规则集合
        Map<String, Set<UserAuthorityRule>> userAuthorityRules = UserAuthorityRuleManager.getUserAuthorityRules();
        if (userAuthorityRules == null) {
            return;
        }
        Set<UserAuthorityRule> forAllResourceRules = userAuthorityRules.get(UserAuthorityConstants.RESOURCE_ALL);
        Set<UserAuthorityRule> forTargetResourceRules = userAuthorityRules.get(resource.getName());
        if (forAllResourceRules.isEmpty() && forTargetResourceRules.isEmpty()) {
            return;
        }

        UserContext userContext = UserContextUtil.getUserContext();
        if (userContext == null || StringUtil.isBlank(userContext.getUserId())) {
            throw new UserAuthorityException("checkUserAuthority invalid user info");
        }

        for (UserAuthorityRule rule : forAllResourceRules) {
            if (!UserAuthorityRuleChecker.passCheck(rule, userContext)) {
                throw new UserAuthorityException(userContext.getUserId(), context.getOrigin(), rule);
            }
        }
        for (UserAuthorityRule rule : forTargetResourceRules) {
            if (!UserAuthorityRuleChecker.passCheck(rule, userContext)) {
                throw new UserAuthorityException(userContext.getUserId(), context.getOrigin(), rule);
            }
        }
    }
}
