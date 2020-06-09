package cn.com.tiza.web.rest.errors;

/**
 * @author tiza
 */
public interface SystemErrorConstants extends ErrorConstants {

    String USERNAME_ALREADY_EXISTS = "user.name.already_used";
    String ROLE_NAME_UNIQUE = "error.role.role_name_unique";

    String USERNAME_PASSWORD_NOT_MATCH = "userLogin.password_not_match";
    String USERNAME_PATCHCA_NOT_MATCH = "login.patchca.not_match";
    String USERNAME_NOT_MATCH = "userLogin.username_not_match";

    String USER_HAS_RELATION_WITH_ROLE = "role.user_has_relation_with_role";

    String ORGANIZATION_NAME_UNIQUE = "error.organization.org_name_unique";

    String FINANCE_NAME_UNIQUE = "error.finance.org_name_unique";

    String SYS_FUNCTION_HAS_CHILDREN = "sysFunctionHasChildren";
    /**
     * 用户被锁定
     */
    String USER_LOCKED = "user.locked";
    String TENANT_IS_DISABLED = "tenant.is.disabled";
}
