package cn.com.tiza.web.app.vm;

import cn.com.tiza.domain.AppConfig;
import cn.com.tiza.dto.UserType;
import lombok.Data;

@Data
public class AppUserVO {
    /**
     * ID
     */
    private Long id;

    private UserType userType;

    private Long financeId;
    /**
     * 登录名
     */
    private String loginName;
    /**
     * 真实姓名
     */
    private String realName;

    private String roleName;
    /**
     * 组织ID
     */
    private Long organizationId;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 电话号码
     */
    private String telephoneNumber;

    /**
     * 电子邮件
     */
    private String emailAddress;

    /**
     * 联系地址
     */
    private String contactAddress;

    private AppConfig appConfig;

    private String orgName;

    private String financeName;
}
