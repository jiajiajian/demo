package cn.com.tiza.web.rest.vm;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Data
public class OrganizationVM {

    private Long id;
    /**
     * 机构代码
     */
    private String orgCode;
    /**
     * 机构名称
     */
    private String orgName;

    private String abbrName;

    private Long orgTypeId;

    private String orgTypeName;
    /**
     * 上级机构ID
     */
    private Long parentOrgId;

    private String parentOrgName;
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
     * 电子邮箱
     */
    private String emailAddress;

    private String contactAddress;
    /**
     * 备注
     */
    private String remark;

    private Long createTime;

    private String path;

    private List<OrganizationVM> children;

    public void add(OrganizationVM obj) {
        if(this.children == null) {
            this.children = new ArrayList<>();
        }
        this.children.add(obj);
    }
}
