package cn.com.tiza.context.access;

/**
 * 检查用户有相关角色
 *
 * @author tiza
 */
public interface RoleCheckHandler {

    /**
     * 检查用户有相关角色
     *
     * @param check  角色
     * @param userID 用户id
     * @return true|false
     */
    boolean check(RoleCheck check, Long userID);

}
