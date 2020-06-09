package cn.com.tiza.domain;

import lombok.Data;
import org.beetl.sql.core.annotatoin.AssignID;
import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.Table;

import java.io.Serializable;
import java.util.List;

/**
 * gen by beetlsql 2020-03-06
 *
 * @author tiza
 */
@Data
@Table(name = "base_finance")
public class Finance extends AbstractEntity implements Entity, Serializable {

    @AutoID
    @AssignID("simple")
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 所在省代码
     */
    private String provinceName;
    /**
     * 所在市代码
     */
    private String cityName;
    /**
     * 联系地址
     */
    private String contactAddress;
    /**
     * 联系人姓名
     */
    private String contactName;
    /**
     * 所在县区代码
     */
    private String countyName;
    /**
     * 电子邮箱
     */
    private String emailAddress;
    /**
     * 传真
     */
    private String faxNo;
    /**
     * 官方网站
     */
    private String officialSite;
    /**
     * 备注
     */
    private String remark;
    /**
     * 电话号码
     */
    private String telephoneNumber;

    private List<Long> orgIds;

    public Finance() {
    }

}
