package com.zhuguang.zhou.pojo;

/**
 * @author liheng
 * @since 1.0
 */
public class ContextUtils {
    private static ThreadLocal<Context> currentLocalContext = new InheritableThreadLocal<>();


    public static Context get() {
        return currentLocalContext.get();
    }

    public static void set(Context context) {
        currentLocalContext.set(context);
    }

    public static void unset() {
        currentLocalContext.remove();
    }

    public static void addGlobalVariable(String key, Object value) {
        Context context = get();
        if (context == null) {
            set(new Context());
            context = get();
        }

        context.addGlobalVariable(key, value);
    }

    public Object getGlobalVariable(String key) {
        Object result = null;

        Context context = get();
        if (context != null) {
            result = context.getGlobalVariable(key);
        }

        return result;
    }

}
