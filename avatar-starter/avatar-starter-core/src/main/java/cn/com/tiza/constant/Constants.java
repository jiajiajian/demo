package cn.com.tiza.constant;

/**
 * 系统常量
 * @author tiza
 */
public interface Constants {

    /**
     * 组织根节点
     */
    Long ROOT_ORG_ID = -1L;

    String DEFAULT_PWD = "123456";
    /**
     * 超级管理员
     */
    String SUPER_ADMIN = "admin";

    String SPRING_PROFILE_DEVELOPMENT = "dev";
    String SPRING_PROFILE_TEST = "test";
    String SPRING_PROFILE_PRODUCTION = "prod";
    /**
     * Spring profile used when deploying with Spring Cloud (used when deploying to CloudFoundry)
     */
    String SPRING_PROFILE_CLOUD = "cloud";

    String AUTHORIZATION_HEADER = "Authorization";

    String USER_INFO_HEADER = "UserInfo";

    String CONTEXT_KEY_USER = "currentUser";
    String CONTEXT_KEY_FINANCE_ID = "currentFinanceId";
    String CONTEXT_KEY_USER_TYPE = "currentUserType";
    String CONTEXT_KEY_USER_ID = "currentUserId";
    String CONTEXT_KEY_ROOT_ORG_ID = "currentRootOrgId";
    String CONTEXT_KEY_ORG_ID = "currentOrgId";
    String CONTEXT_KEY_LOGIN_NAME = "currentLoginName";
    String CONTEXT_KEY_USER_NAME = "currentUserName";
    String CONTEXT_KEY_USER_TOKEN = "currentUserToken";
    String CONTEXT_KEY_USER_IP = "currentUserIP";
    String CONTEXT_KEY_USER_AGENT = "currentUserAgent";

    /**
     * 自定义报警类型
     */
    String SELF_DEFINING_ALARM = "SELF_DEFINING_ALARM_LEVEL";
}
