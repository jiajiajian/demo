package cn.com.tiza.domain;

import lombok.Data;
import org.beetl.sql.core.annotatoin.Table;

/**
 * @author tiza
 */
@Data
@Table(name = "base_user")
public class User {

    private Long id;
    /**
     * 登录名
     */
    private String loginName;
    private Long organizationId;
    /**
     * 真实姓名
     */
    private String realname;

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

    private String appConfig;

    /**
     * =====非数据库字段=========
     */
    private Long vehicleModelId;

}
