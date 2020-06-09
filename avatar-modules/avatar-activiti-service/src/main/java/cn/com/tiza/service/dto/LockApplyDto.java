package cn.com.tiza.service.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;


/**
 * gen by beetlsql 2020-04-27
 *
 * @author tiza
 */
@Data
public class LockApplyDto {

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

    public LockApplyDto() {
    }

    @NotEmpty
    private List<String> vinList;

}
