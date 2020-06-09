package cn.com.tiza.web.rest.errors;

import static cn.com.tiza.web.rest.errors.SystemErrorConstants.USER_LOCKED;

/**
 * @author TZ0781
 */
public class UserLockedException extends BadRequestAlertException {

    public UserLockedException(String param) {
        super("", param, USER_LOCKED);
    }

    public UserLockedException(String entityName, String errorKey) {
        super("", entityName, errorKey);
    }
}
