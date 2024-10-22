package top.kwseeker.msa.action.sentinel.slots.block.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.csp.sentinel.log.RecordLog;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.csp.sentinel.property.DynamicSentinelProperty;
import com.alibaba.csp.sentinel.property.PropertyListener;
import com.alibaba.csp.sentinel.property.SentinelProperty;

/**
 * 从配置中心加载用户黑白名单规则
 */
public class UserAuthorityRuleManager {

    // 资源名 -> 对应的用户黑白名单规则集合，针对全部资源的规则的key为 RESOURCE_ALL = ""
    private static volatile Map<String, Set<UserAuthorityRule>> userAuthorityRules = new ConcurrentHashMap<>();
    // UserAuthorityRule配置数据的监听器，Nacos中配置变更以及初始化后会获取规则配置并调用所有监听器，设置到各个ProcessorSlot中去
    private static final UserAuthorityRuleManager.RulePropertyListener LISTENER = new UserAuthorityRuleManager.RulePropertyListener();
    // 使用Nacos配置中心时，这个属性实例实际不会被用到，因为在 SentinelDataSourceHandler 中会将 currentProperty 重新指向配置中心属性源中的 SentinelProperty 实例
    // 就是通过下面 register2Property() 重新指向的
    private static SentinelProperty<List<UserAuthorityRule>> currentProperty = new DynamicSentinelProperty<>();

    static {
        currentProperty.addListener(LISTENER);
    }

    public static void register2Property(SentinelProperty<List<UserAuthorityRule>> property) {
        AssertUtil.notNull(property, "property cannot be null");
        synchronized (LISTENER) {
            if (currentProperty != null) {
                currentProperty.removeListener(LISTENER);
            }
            property.addListener(LISTENER);
            //替换本地的 SentinelProperty 实例为配置中心数据源（比如 NacosDataSource）中的 SentinelProperty 实例
            currentProperty = property;
            RecordLog.info("[UserAuthorityRuleManager] Registering new property to user authority rule manager");
        }
    }

    /**
     * Load the user authority rules to memory.
     *
     * @param rules list of user authority rules
     */
    public static void loadRules(List<UserAuthorityRule> rules) {
        currentProperty.updateValue(rules);
    }

    /**
     * 是否存在专门针对某资源的用户黑白名单规则
     */
    public static boolean hasConfig(String resource) {
        return userAuthorityRules.containsKey(resource);
    }

    /**
     * Get a copy of the rules.
     *
     * @return a new copy of the rules.
     */
    public static List<UserAuthorityRule> getRules() {
        List<UserAuthorityRule> rules = new ArrayList<>();
        if (userAuthorityRules == null) {
            return rules;
        }
        for (Map.Entry<String, Set<UserAuthorityRule>> entry : userAuthorityRules.entrySet()) {
            rules.addAll(entry.getValue());
        }
        return rules;
    }

    /**
     * 当配置中心规则数据改变后，通过此监听器，将配置中心的数据同步到 UserAuthorityRuleManager 本地
     */
    private static class RulePropertyListener implements PropertyListener<List<UserAuthorityRule>> {

        @Override
        public synchronized void configLoad(List<UserAuthorityRule> value) {
            userAuthorityRules = loadAuthorityConf(value);

            RecordLog.info("[UserAuthorityRuleManager] Authority rules loaded: {}", userAuthorityRules);
        }

        @Override
        public synchronized void configUpdate(List<UserAuthorityRule> conf) {
            userAuthorityRules = loadAuthorityConf(conf);

            RecordLog.info("[UserAuthorityRuleManager] Authority rules received: {}", userAuthorityRules);
        }

        private Map<String, Set<UserAuthorityRule>> loadAuthorityConf(List<UserAuthorityRule> list) {
            Map<String, Set<UserAuthorityRule>> newRuleMap = new ConcurrentHashMap<>();

            if (list == null || list.isEmpty()) {
                return newRuleMap;
            }

            for (UserAuthorityRule rule : list) {
                if (!isValidRule(rule)) {
                    RecordLog.warn("[UserAuthorityRuleManager] Ignoring invalid user authority rule when loading new rules: {}", rule);
                    continue;
                }

                String identity = rule.getResource();
                Set<UserAuthorityRule> ruleSet = newRuleMap.get(identity);
                // putIfAbsent
                if (ruleSet == null) {
                    ruleSet = new HashSet<>();
                    ruleSet.add(rule);
                    newRuleMap.put(identity, ruleSet);
                } else {
                    // 每个资源的用户黑白名单规则只应该有一个
                    RecordLog.warn("[UserAuthorityRuleManager] Ignoring redundant rule: {}", rule.toString());
                }
            }

            return newRuleMap;
        }

    }

    static Map<String, Set<UserAuthorityRule>> getUserAuthorityRules() {
        return userAuthorityRules;
    }

    public static boolean isValidRule(UserAuthorityRule rule) {
        return rule != null && rule.getStrategy() >= 0 && StringUtil.isNotBlank(rule.getLimitUsers());
    }
}
