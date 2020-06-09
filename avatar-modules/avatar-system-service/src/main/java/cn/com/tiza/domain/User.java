package cn.com.tiza.domain;

import cn.com.tiza.dto.UserType;
import cn.com.tiza.web.rest.errors.BadRequestException;
import cn.com.tiza.web.rest.errors.ErrorConstants;
import lombok.Data;
import org.beetl.sql.core.annotatoin.AssignID;
import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.Table;

import java.io.Serializable;
import java.util.List;

/**
 * @author tiza
 */
@Data
@Table(name = "base_user")
public class User extends AbstractAuditingEntity implements Entity, BaseUser, Serializable {
    private static final long serialVersionUID = -8402838239687848510L;
    /**
     * 主键自动生成
     */
    @AutoID
    @AssignID("simple")
    private Long id;
    /**
     * 登录名
     */
    private String loginName;
    /**
     * 登录密码
     */
    private String loginPassword;

    /**
     * 用户类型：1=管理员、2=机构用户, 3=融资用户
     */
    private UserType userType;
    /**
     * 融资机构ID
     */
    private Long financeId;


    private Long organizationId;
    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 角色
     */
    private String roleName;

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

    /**
     * 备注
     */
    private String remark;

    /**
     * 过期时间
     */
    private Long expirationDate;

    /**
     * 登录次数
     */
    private Integer loginTimes;

    /**
     * 最后登录IP
     */
    private String latestLoginIp;
    /**
     * 最后登录时间
     */
    private Long latestLoginTime;

    private String appConfig;

    /* view fields */
    /**
     * 角色ID
     */
    private List<Long> roleIds;

    private Long rootOrgId;

    private String orgName;

    private String financeName;

    public void checkEnable() {
        if (Boolean.FALSE.equals(super.getEnableFlag())) {
            throw new BadRequestException(ErrorConstants.USER_ALREADY_DISABLED);
        }
        if (this.expirationDate != null && this.expirationDate <= System.currentTimeMillis()) {
            throw new BadRequestException(ErrorConstants.USER_ALREADY_DISABLED);
        }
    }

}
