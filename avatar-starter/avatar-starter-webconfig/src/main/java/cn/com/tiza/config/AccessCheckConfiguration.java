package cn.com.tiza.config;

import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.context.access.*;
import cn.com.tiza.web.rest.errors.RequirePermissionException;
import cn.com.tiza.web.rest.errors.RequireRoleException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: TZ0781
 * 权限检查
 **/
@Aspect
@Component
@Slf4j
public class AccessCheckConfiguration {

    @Autowired(required = false)
    private PermissionCheckHandler handler;

    @Autowired(required = false)
    private RoleCheckHandler roleCheckHandler;

    /**
     * check user has permission
     * @param permissionCheck permissions
     */
    @Before("@annotation(permissionCheck)")
    public void permissionCheck(PermissionCheck permissionCheck) {
        log.debug("check permission value: {}, description {}", permissionCheck.value(), permissionCheck.description());
        Long userId = BaseContextHandler.getUserID();
        if (userId == null) {
            log.debug("method need user login!");
            throw new RequirePermissionException("error.permission.login", permissionCheck.value());
        }
        if (handler != null) {
            if (handler.check(permissionCheck, userId)) {
                return;
            }
            throw new RequirePermissionException("error.permission.forbidden", permissionCheck.value());
        }
    }

    /**
     * check user has role
     * @param roleCheck roles
     */
    @Before("@annotation(roleCheck)")
    public void roleCheck(RoleCheck roleCheck) {
        log.debug("check role value: {}, description {}", roleCheck.value(), roleCheck.description());
        Long userId = BaseContextHandler.getUserID();
        if (userId == null) {
            log.debug("method need user login!");
            throw new RequireRoleException("error.not_login", roleCheck.value());
        }
        if (roleCheckHandler != null) {
            if (roleCheckHandler.check(roleCheck, userId)) {
                return;
            }
            throw new RequireRoleException("error.role.forbidden", roleCheck.value());
        }
    }

    /**
     * check user
     * @param userCheck users
     */
    @Before("@annotation(userCheck)")
    public void userCheck(UserCheck userCheck) {
        log.debug("check user value: {}, description {}", userCheck.value(), userCheck.description());
        String loginName = BaseContextHandler.getLoginName();
        if (loginName == null) {
            log.debug("method need user login!");
            throw new RequireRoleException("error.not_login", userCheck.value());
        }
        boolean match = false;
        for (String s : userCheck.value()) {
            if(s.equalsIgnoreCase(loginName)) {
                match = true;
                break;
            }
        }
        if (! match) {
            throw new RequireRoleException("error.user.forbidden", userCheck.value());
        }
    }
}
