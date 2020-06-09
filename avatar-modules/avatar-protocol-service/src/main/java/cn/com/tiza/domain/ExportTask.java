package cn.com.tiza.domain;

import org.beetl.sql.core.annotatoin.AssignID;
import org.beetl.sql.core.annotatoin.Table;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author villas
 */
@Data
@Table(name = "FWP_EXPORT_TASK")
public class ExportTask implements Serializable {

    public static final int ORIGINAL = 0;

    public static final int RUNNING = 1;

    public static final int SUCCESS = 2;

    public static final int FAILED = 3;

    @AssignID("simple")
    private Long id;
    private Long beginTime;
    private Long endTime;
    private String filePath;
    private Long fwpTaskId;
    private String name;
    private Integer status;
    private Integer dataType;
    private Integer vehicleAmount;

    /**
     * 实际运行时间
     */
    private Date runTime;
}
