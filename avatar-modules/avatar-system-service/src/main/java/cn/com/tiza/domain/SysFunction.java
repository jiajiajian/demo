package cn.com.tiza.domain;

import lombok.Data;
import org.beetl.sql.core.annotatoin.AssignID;
import org.beetl.sql.core.annotatoin.Table;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author tiza
 */
@Data
@Table(name = "base_sys_function")
public class SysFunction implements Serializable {
    private static final long serialVersionUID = 2285179148192161938L;
    /**
     * 主键ID
     */
    @AssignID
    private Long id;
    /**
     * 功能代码
     */
    private String functionCode;
    /**
     * 功能名称
     */
    private String functionName;
    /**
     * 上级功能ID
     */
    private Long parentFunctionId;
    /**
     * 层级(平台级多系统时使用。0.平台；1.系统；2.功能)
     */
    private Integer layerLevel;
    /**
     * 功能类型(0.功能组；1.功能;2.操作)
     */
    private Integer functionType;

    /**
     * 1=ADMIN, 2=Tenant_ADMIN, 3=Normal
     */
    private Integer roleType;
    /**
     * 导航链接
     */
    private String naviUrl;
    /**
     * 图标
     */
    private String icon;
    /**
     * 是否打开新页面
     */
    private Boolean isNewPage;
    /**
     * 排序码
     */
    private Integer sortCode;
    /**
     * 备注
     */
    private String remark;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SysFunction sysFunction = (SysFunction) o;
        return Objects.equals(id, sysFunction.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
