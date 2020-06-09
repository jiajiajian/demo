package cn.com.tiza.service.dto;

import cn.com.tiza.dto.UserType;
import cn.com.tiza.web.rest.errors.BadRequestException;
import lombok.Data;
import org.apache.commons.lang3.Validate;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;

/**
 * @author tiza
 */
@Data
public class UserCommand {
    /**
     * 登录名
     */
    @NotBlank
    private String loginName;
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
    @NotBlank
    private String realName;

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
     * 角色ID列表
     */
    private List<Long> roleIds;

    public void validateUserType() {
        Validate.notNull(userType, "user.type can not null");
        if(UserType.FINANCE.equals(userType)) {
            if(Objects.isNull(financeId)) {
                throw new BadRequestException("error.user.finance.can_not_null");
            }
            Validate.isTrue(organizationId == null);
        } else if(UserType.ORGANIZATION.equals(userType)) {
            if(Objects.isNull(organizationId)) {
                throw new BadRequestException("error.user.organization.can_not_null");
            }
            Validate.isTrue(financeId == null);
        } else {
            if(! Objects.isNull(financeId) || ! Objects.isNull(organizationId)) {
                throw new BadRequestException("error.user.admin.no_org_or_finance");
            }
        }
    }
}
