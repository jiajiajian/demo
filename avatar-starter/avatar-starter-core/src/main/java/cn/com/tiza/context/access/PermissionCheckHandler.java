package cn.com.tiza.context.access;

/**
 * 权限检查并记录用户访问
 * @author tiza
 */
public interface PermissionCheckHandler {

	boolean check(PermissionCheck check, Long userID);

}
