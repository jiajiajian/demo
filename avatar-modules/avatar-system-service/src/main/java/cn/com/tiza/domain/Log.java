package cn.com.tiza.domain;

import lombok.Data;
import org.beetl.sql.core.annotatoin.AssignID;
import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.Table;

/**
 * @author tiza
 */
@Data
@Table(name = "base_log")
public class Log {
    /**
     * 主键自动生成
     */
    @AutoID
    @AssignID("simple")
    private Long id;
    /**
     * 日志类型：0.异常；1.登录；2.操作
     */
    private Integer logType;
    /**
     * 租户ID
     */
    private Long tenantId;
    /**
     * 所属机构ID
     */
    private Long organizationId;
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
}
