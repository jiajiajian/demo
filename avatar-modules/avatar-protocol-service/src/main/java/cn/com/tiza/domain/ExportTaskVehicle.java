package cn.com.tiza.domain;

import org.beetl.sql.core.annotatoin.AssignID;
import org.beetl.sql.core.annotatoin.Table;

import lombok.Data;
import java.io.Serializable;

/**
 * 
 * @author villas
 */
@Data
@Table(name = "FWP_EXPORT_TASK_VEHICLE")
public class ExportTaskVehicle implements Serializable {
    @AssignID("simple")
    private Long id;
    private Long exportTaskId;
    private String vin;
}
