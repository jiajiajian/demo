package cn.com.tiza.web.rest.errors;

import java.util.Map;

/**
 * 用户名密码不匹配
 */
public class UsernamePasswordNotMatchException extends BadRequestAlertException implements SystemErrorConstants {

    private static final long serialVersionUID = 1L;

    public UsernamePasswordNotMatchException(Map<String, Object> params) {
        super(params, USERNAME_PASSWORD_NOT_MATCH);
    }
}