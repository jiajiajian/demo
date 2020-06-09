package cn.com.tiza.web.rest.errors;

import cn.com.tiza.errors.AbstractThrowableProblem;
import cn.com.tiza.errors.Status;

import java.util.HashMap;
import java.util.Map;

/**
 * 没有权限异常
 */
public class RequireRoleException extends AbstractThrowableProblem {

    public RequireRoleException(String errorKey, String[] value) {
        super("Permission is forbidden", Status.FORBIDDEN, null, getAlertParameters(errorKey, value));
    }

    private static Map<String, Object> getAlertParameters(String errorKey, String[] value) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("message", errorKey);
        parameters.put("params", value);
        return parameters;
    }
}
