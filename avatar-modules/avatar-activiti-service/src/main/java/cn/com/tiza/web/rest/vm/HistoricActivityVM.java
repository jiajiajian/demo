package cn.com.tiza.web.rest.vm;

import lombok.Data;
import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntityImpl;

@Data
public class HistoricActivityVM extends HistoricActivityInstanceEntityImpl {

    /** 处理意见 */
    private String comment;

    /** 审批人 */
    private String assigneeName;

    /** 执行动作 */
    private String action;
}
