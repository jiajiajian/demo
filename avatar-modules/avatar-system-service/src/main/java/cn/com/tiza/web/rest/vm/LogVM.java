package cn.com.tiza.web.rest.vm;

import lombok.Data;

/**
 * @author 0837
 * 返回前台的实体类
 */
@Data
public class LogVM {
    /**
     *日志类型：0.异常；1.登录；2.操作
     */
    private Integer logType;
    /**
     *操作时间
     */
    private Long operateTime;
    /**
     *操作账号
     */
    private String operateAccount;
    /**
     *系统功能名称
     */
    private String functionName;
    /**
     *操作内容
     */
    private String operateContent;
    /**
     *IP地址
     */
    private String ipAddress;
}
