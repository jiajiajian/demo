package cn.com.tiza.domain;

import cn.com.tiza.constant.Constants;
import cn.com.tiza.dto.UserType;

/**
 * @author tiza
 */
public interface BaseUser {

    /**
     * 是否融资用户
     *
     * @return boolean
     */
    default boolean isFinance() {
        return UserType.FINANCE.equals(getUserType());
    }

    /**
     * 是否管理员
     *
     * @return boolean
     */
    default boolean isAdmin() {
        return UserType.ADMIN.equals(getUserType());
    }

    /**
     * 是否机构用户
     *
     * @return boolean
     */
    default boolean isOrganization() {
        return UserType.ORGANIZATION.equals(getUserType());
    }

    /**
     * 是否超级管理员
     *
     * @return boolean
     */
    default boolean isSuperAdmin() {
        return Constants.SUPER_ADMIN.equalsIgnoreCase(getLoginName());
    }

    /**
     * 用户类型
     *
     * @return UserType
     */
    UserType getUserType();

    /**
     * 用户登录名
     *
     * @return
     */
    String getLoginName();
}
