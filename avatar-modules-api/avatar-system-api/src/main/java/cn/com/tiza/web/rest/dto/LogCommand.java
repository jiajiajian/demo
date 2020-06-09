package cn.com.tiza.web.rest.dto;

import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.context.UserInfo;
import lombok.Data;

/**
 * 操作日志Command
 *
 * @author tiza
 */
@Data
public class LogCommand {
    /**
     * 日志类型：0.异常；1.登录；2.操作,3=退出
     */
    private Integer logType;
    /**
     * 操作时间
     */
    private Long operateTime;
    /**
     * 操作账号
     */
    private String operateAccount;
    /**
     * 操作用户姓名
     */
    private String operateRealName;
    /**
     * 系统功能ID
     */
    private Long functionId;
    /**
     * 系统功能名称
     */
    private String functionName;
    /**
     * 操作内容
     */
    private String operateContent;
    /**
     * IP地址
     */
    private String ipAddress;
    /**
     * IP地址所在城市
     */
    private String ipCity;
    /**
     * 主机
     */
    private String hostName;
    /**
     * 浏览器
     */
    private String browserName;
    /**
     * 所属机构ID
     */
    private Long organizationId;
    /**
     * 所属融资机构ID
     */
    private Long financeId;

    public LogCommand() {
    }

    public LogCommand(Integer logType, String content, UserInfo user) {
        this.logType = logType;
        if(user.isOrganization()) {
            this.organizationId = user.getOrgId();
        } else if(user.isFinance()) {
            this.financeId = user.getFinanceId();
        }
        this.operateAccount = user.getLoginName();
        this.operateRealName = user.getRealName();
        this.operateTime = System.currentTimeMillis();
        this.operateContent = content;
        this.ipAddress = BaseContextHandler.getIpAddress();
        this.browserName = BaseContextHandler.getUserAgent();
    }
}
