package cn.com.tiza.web.rest.vm;

import lombok.Data;

/**
 * 保养项详细信息
 * @author tiza
 */
@Data
public class MaintenanceContentVM {
    private String maintenanceContent;
    private Integer hours;
    private Integer maintenanceType;
    private Long tacticsId;
}
