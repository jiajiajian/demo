package cn.com.tiza.context;

import cn.com.tiza.constant.Constants;
import cn.com.tiza.dto.UserType;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户信息上下文
 *
 * @author tiza
 */
@Slf4j
public class BaseContextHandler {
    public static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();

    public static void set(String key, Object value) {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<>(10);
            threadLocal.set(map);
        }
        map.put(key, value);
    }

    public static Object get(String key) {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<>(10);
            threadLocal.set(map);
        }
        return map.get(key);
    }

    public static String getValue(String key) {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<>(10);
            threadLocal.set(map);
        }
        return returnObjectValue(map.get(key));
    }

    public static Long getLongValue(String key) {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<>(10);
            threadLocal.set(map);
        }
        return returnLongValue(map.get(key));
    }

    public static UserInfo getUser() {
        return (UserInfo) get(Constants.CONTEXT_KEY_USER);
    }

    public static Long getUserID() {
        return getLongValue(Constants.CONTEXT_KEY_USER_ID);
    }

    public static Long getOrgId() {
        return getLongValue(Constants.CONTEXT_KEY_ORG_ID);
    }

    public static Long getRootOrgId() {
        return getLongValue(Constants.CONTEXT_KEY_ROOT_ORG_ID);
    }

    public static Long getFinanceId() {
        return getLongValue(Constants.CONTEXT_KEY_FINANCE_ID);
    }

    public static String getLoginName() {
        return getValue(Constants.CONTEXT_KEY_LOGIN_NAME);
    }

    public static String getName() {
        return getValue(Constants.CONTEXT_KEY_USER_NAME);
    }

    public static String getToken() {
        return getValue(Constants.CONTEXT_KEY_USER_TOKEN);
    }

    public static String getIpAddress() {
        return getValue(Constants.CONTEXT_KEY_USER_IP);
    }

    public static String getUserAgent() {
        return getValue(Constants.CONTEXT_KEY_USER_AGENT);
    }

    public static String getUserType() {
        return getValue(Constants.CONTEXT_KEY_USER_TYPE);
    }

    public static void setToken(String token) {
        set(Constants.CONTEXT_KEY_USER_TOKEN, token);
    }

    public static void setUser(UserInfo user) {
        set(Constants.CONTEXT_KEY_USER, user);
    }

    public static void setUserType(UserType userType) {
        set(Constants.CONTEXT_KEY_USER_TYPE, userType);
    }

    public static void setName(String name) {
        set(Constants.CONTEXT_KEY_USER_NAME, name);
    }

    public static void setUserID(Long userID) {
        set(Constants.CONTEXT_KEY_USER_ID, userID);
    }

    public static void setRootOrgId(Long orgId) {
        set(Constants.CONTEXT_KEY_ROOT_ORG_ID, orgId);
    }

    public static void setOrgId(Long orgId) {
        set(Constants.CONTEXT_KEY_ORG_ID, orgId);
    }

    public static void setFinanceId(Long financeId) {
        set(Constants.CONTEXT_KEY_FINANCE_ID, financeId);
    }

    public static void setLoginName(String loginName) {
        set(Constants.CONTEXT_KEY_LOGIN_NAME, loginName);
    }

    public static void setIPAddress(String ipAddress) {
        set(Constants.CONTEXT_KEY_USER_IP, ipAddress);
    }

    public static void setUserAgent(String agent) {
        set(Constants.CONTEXT_KEY_USER_AGENT, agent);
    }

    private static String returnObjectValue(Object value) {
        return value == null ? null : value.toString();
    }

    private static Long returnLongValue(Object value) {
        return value == null || value.equals("") ? null : Long.valueOf(value.toString());
    }

    public static void remove() {
        threadLocal.remove();
    }

}
