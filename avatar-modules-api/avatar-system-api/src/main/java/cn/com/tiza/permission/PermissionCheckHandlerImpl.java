package cn.com.tiza.permission;

import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.context.UserInfo;
import cn.com.tiza.context.access.PermissionCheck;
import cn.com.tiza.context.access.PermissionCheckHandler;
import cn.com.tiza.web.rest.AccountApiClient;
import cn.com.tiza.web.rest.dto.LogCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 权限检查实现类
 *
 * @author: TZ0781
 **/
@Service
public class PermissionCheckHandlerImpl implements PermissionCheckHandler {

    @Autowired
    private AccountApiClient accountApiClient;

    @Override
    public boolean check(PermissionCheck check, Long userID) {
        UserInfo user = BaseContextHandler.getUser();
        if (user.isSuperAdmin()) {
            logOperator(check, user);
            return true;
        }

        boolean permission = accountApiClient.checkPermission(check.value());
        if (permission) {
            logOperator(check, user);
        }
        return permission;
    }

    /**
     * 保存用户操作日志 只保存get请求以外log日志
     */
    private void logOperator(PermissionCheck check, UserInfo user) {
        accountApiClient.create(new LogCommand(2, check.description(), user));
    }
}
