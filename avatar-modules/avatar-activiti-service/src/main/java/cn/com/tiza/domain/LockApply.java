package cn.com.tiza.domain;

import lombok.Data;
import org.beetl.sql.core.annotatoin.AssignID;
import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.Table;

import java.io.Serializable;

/**
 * gen by beetlsql 2020-04-27
 *
 * @author tiza
 */
@Data
@Table(name = "r_lock_apply")
public class LockApply implements Serializable {

    @AutoID
    @AssignID("simple")
    /**
     * 主键
     */
    private Long id;
    /**
     * 申请状态（0.待审批，1.审批中，2审批通过， 3.审批未通过）
     */
    private Integer state;
    /**
     * 申请单号
     */
    private String applyCode;
    /**
     * 申请人
     */
    private String applyUser;
    /**
     * 创建时间--申请时间
     */
    private Long createTime;
    /**
     * 流程实例ID
     */
    private String instanceId;
    /**
     * 机构id
     */
    private Long orgId;
    /**
     * 申请原因
     */
    private String reason;

    /**
     * 过期时间
     */
    private Long expireTime;

    public LockApply() {
    }

}
