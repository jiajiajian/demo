package cn.com.tiza.domain;

import cn.com.tiza.dto.RoleType;
import lombok.Data;
import org.beetl.sql.core.annotatoin.AssignID;
import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.Table;

import java.io.Serializable;

/**
 * @author jiajian
 */
@Data
@Table(name = "base_role")
public class Role extends AbstractEntity implements Entity, Serializable {
    private static final long serialVersionUID = 5917621105345036537L;
    /**
     * 主键ID
     */
    @AutoID
    @AssignID("simple")
    private Long id;
    /**
     * 角色类型: 机构角色，融资机构角色
     */
    private RoleType roleType ;

    /**
     * 融资机构ID
     */
    private Long financeId ;

    /**
     * 机构ID
     */
    private Long organizationId;
    /**
     * 角色代码
     */
    private String roleCode;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 备注
     */
    private String remark;
    /**
     * 过期时间
     */
    private Long expirationDate;

    public Role() {
    }
}
