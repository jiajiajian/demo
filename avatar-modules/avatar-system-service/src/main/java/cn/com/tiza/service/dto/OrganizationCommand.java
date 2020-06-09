package cn.com.tiza.service.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class OrganizationCommand {
    /**
     * 机构代码
     */
    private String orgCode;
    /**
     *机构名称
     */
    @NotBlank
    private String orgName;
    /**
     * 上级机构ID
     */
    private Long parentOrgId;

    private Long orgTypeId;
    /**
     * 机构简称
     */
    private String abbrName ;

    /**
     * 联系人姓名
     */
    private String contactName;
    /**
     * 传真
     */
    private String faxNo;
    /**
     * 电话号码
     */
    private String telephoneNumber;
    /**
     * 邮件地址
     */
    private String emailAddress;
    /**
     * 省
     */
    private String provinceName;
    /**
     * 市
     */
    private String cityName;
    /**
     * 所在县区代码
     */
    private String countyName;
    /**
     * 联系地址
     */
    private String contactAddress;
    /**
     * 备注
     */
    private String remark;
}
