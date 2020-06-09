package cn.com.tiza.web.rest.vm;

import lombok.Data;

import java.util.Date;

@Data
public class TaskVM{
    /** 申请人姓名 */
    private String applyUserName;

    /** 任务ID */
    private String taskId;

    /** 任务名称 */
    private String taskName;

    /** 办理时间 */
    private Date doneTime;

    /** 申请时间*/
    private Long applyTime;

    private String applyCode;

    private String reason;

    private String instanceId;

    /**执行操作*/
    private String action;
}
