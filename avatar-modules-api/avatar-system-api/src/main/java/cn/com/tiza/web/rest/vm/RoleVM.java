package cn.com.tiza.web.rest.vm;

import cn.com.tiza.dto.RoleType;
import lombok.Data;

/**
 * @author jiajian
 */
@Data
public class RoleVM {
    private Long id;
    /**
     * 角色类型: 机构角色，融资机构角色
     */
    private RoleType roleType ;

    /**
     * 融资机构ID
     */
    private Long financeId ;

    private String financeName;
    /**
     * 机构ID
     */
    private Long organizationId;

    private String orgName;

    /**
     * 角色名称
     **/
    private String roleName;

    /**
     * 备注
     **/
    private String remark;

    /**
     * 创建人
     **/
    private String  createUserAccount;

    /**
     * 创建时间
     **/
    private Long  createTime;



}
