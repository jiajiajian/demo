package cn.com.tiza.domain;

import lombok.Data;
import org.beetl.sql.core.annotatoin.AssignID;
import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.Table;

/**
 * @author jiajian
 */
@Data
@Table(name = "base_role")
public class Role  {
    /**
     * 主键ID
     */
    @AutoID
    @AssignID("simple")
    private Long id;
    /**
     * 角色代码
     */
    private String roleCode;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 所属机构ID
     */
    private Long organizationId;
    private Long tenantId;

}
