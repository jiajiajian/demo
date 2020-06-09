package cn.com.tiza.domain;

import lombok.Data;
import org.beetl.sql.core.annotatoin.AssignID;
import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.Table;

import java.io.Serializable;

/**
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Data
@Table(name = "v_alarm_item")
public class AlarmItem implements Serializable {

    @AutoID
    @AssignID("simple")
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 报警项
     */
    private String alarmItem;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 创建用户登录名
     */
    private String createUserAccount;
    /**
     * 创建用户姓名
     */
    private String createUserRealname;
    /**
     * 更新时间
     */
    private Long updateTime;
    /**
     * 更新用户登录名
     */
    private String updateUserAccount;
    /**
     * 更新用户姓名
     */
    private String updateUserRealname;
    /**
     * 机构ID
     */
    private Long organizationId;
    /**
     * 备注
     */
    private String remark;

    public AlarmItem() {
    }

}
