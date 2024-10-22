package top.kwseeker.msa.action.sentinel.context;

import top.kwseeker.msa.action.framework.common.context.UserContext;

public class UserContextUtil {

    private static final ThreadLocal<UserContext> userContextHolder = new ThreadLocal<>();

    public static void enter(UserContext userContext) {
        userContextHolder.set(userContext);
    }

    public static UserContext getUserContext() {
        return userContextHolder.get();
    }

    public static void exit() {
        userContextHolder.remove();
    }
}
