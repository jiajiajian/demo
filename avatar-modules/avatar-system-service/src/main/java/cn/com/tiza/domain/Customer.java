package cn.com.tiza.domain;

import lombok.Data;
import org.beetl.sql.core.annotatoin.AssignID;
import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.Table;

import java.io.Serializable;

/**
 * gen by beetlsql 2020-03-06
 *
 * @author tiza
 */
@Data
@Table(name = "base_customer")
public class Customer extends AbstractEntity implements Entity, Serializable {

    @AutoID
    @AssignID("simple")
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 报警联系人
     */
    private String alarmName;
    /**
     * 24H报警电话
     */
    private String alarmNumber;
    /**
     * 登录名
     */
    private String name;
    /**
     * 组织ID
     */
    private Long organizationId;
    /**
     * 机手名
     */
    private String ownerName;
    /**
     * 机手电话
     */
    private String ownerNumber;
    /**
     * 手机号码
     */
    private String phoneNumber;
    /**
     * 备注
     */
    private String remark;
    /**
     * 电话号码
     */
    private String telephoneNumber;

    public Customer() {
    }

}
