package cn.com.tiza.domain;

import cn.com.tiza.constant.Constants;
import lombok.Data;
import org.beetl.sql.core.annotatoin.AssignID;
import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.Table;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author tiza
 */
@Data
@Table(name = "base_organization")
public class Organization extends AbstractEntity implements Entity, Serializable {

    private static final long serialVersionUID = -7938115632931088392L;
    @AutoID
    @AssignID("simple")
    private Long id;
    /**
     * 机构代码
     */
    private String orgCode;
    /**
     * 机构名称
     */
    private String orgName;
    /**
     * 机构简称
     */
    private String abbrName ;

    /**
     * 上级机构ID
     */
    private Long parentOrgId;

    private Long rootOrgId;
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
    /**
     * 所在省
     */
    private String provinceName;
    /**
     * 所在市
     */
    private String cityName;
    /**
     * 所在县区
     */
    private String countyName;
    /**
     * 联系地址
     */
    private String contactAddress;
    /**
     * 官方网站
     */
    private String officialSite;
    /**
     * 机构类型。关联数据字典代码ORGANIZATION_TYPE下属明细项ID
     */
    private Long orgTypeId;

    /**
     * 完整路径
     */
    private String path;
    /**
     * 备注
     */
    private String remark;

    /**
     * 判断组织是否有上级
     * @return
     */
    public boolean hasParent() {
        return parentOrgId != null && ! Objects.equals(parentOrgId, Constants.ROOT_ORG_ID) ;
    }

    public boolean isRoot() {
        return parentOrgId == null || Objects.equals(parentOrgId, Constants.ROOT_ORG_ID) ;
    }

    /**
     * 生成PATH路径
     * @param parent
     * @return
     */
    public String genPath(Organization parent) {
        if(parent == null) {
            return id + "/";
        }
        if(null == parent.getPath() || parent.getPath().trim().isEmpty()) {
            return parent.getId() + "/" + id + "/";
        }
        return parent.getPath() + id + "/";
    }

    public boolean isParent(Long parentOrgId) {
        return this.path.contains("/" + parentOrgId + "/");
    }
}
