package cn.com.tiza.domain;

import lombok.Data;
import org.beetl.sql.core.annotatoin.AssignID;
import org.beetl.sql.core.annotatoin.Table;

import java.io.Serializable;

/**
 * 
 * @author villas
 */
@Data
@Table(name = "FWP_TASK_PARAM")
public class TaskParam implements Serializable {
    @AssignID("simple")
    private Long id;
    private Long createAt;
    private String varValue;
    private Long taskId;
    private String varCode;
}
