package cn.com.tiza.domain;

import lombok.Data;
import org.beetl.sql.core.annotatoin.AssignID;
import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.Table;

import java.io.Serializable;

/**
 * @author villas
 */
@Data
@Table(name = "fwp_task_data")
public class TaskData implements Serializable {

    @AutoID
    @AssignID("simple")
    private Long id;

    private Long dataAmount;

    private Integer dataType;

    private Long taskId;

    private Long statisticalTime;

    private Long totalDataAmount;
    /**
     * 收到帧数
     */
    private Long revAmount;
    /**
     * 转发成功帧数
     */
    private Long sucAmount;
    /**
     * 转发失败帧数
     */
    private Long faiAmount;
    /**
     * 补传帧数
     */
    private Long patAmount;

    private Long vehicleAmount;

    private String name;

    private Long createAt;
}
