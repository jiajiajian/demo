package cn.com.tiza.domain;

import lombok.Data;
import org.beetl.sql.core.annotatoin.AssignID;
import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.Table;

import java.math.BigDecimal;

/**
 * @author jiajian
 */
@Data
@Table(name = "base_authorize")
public class Authorize {
    /**
     * 主键ID
     */
    @AutoID
    @AssignID("simple")
    private Long id;

    /**
     * 授权对象类型。(预留多种赋权方式，默认0，对角色赋权)
     */
    private Integer authorizeObjectType;

    /**
     * 授权对象ID
     */
    private Long authorizeObjectId;

    /**
     * 权限项类型。0.平台；1.系统；2.功能；3.操作
     */
    private Integer permissionType;

    /**
     * 权限项ID
     */
    private Long permissionId;

    /**
     * 授权时间
     */
    private Long authorizeTime;

    /**
     * 授权账号
     */
    private String authorizeAccount;

    /**
     * 授权用户姓名
     */
    private String authorizeRealname;

    /**
     * 选中状态 0半选 1全选
     */
    private Integer checkStatus;
}
