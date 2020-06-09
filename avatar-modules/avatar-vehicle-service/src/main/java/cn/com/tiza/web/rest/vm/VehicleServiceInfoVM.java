package cn.com.tiza.web.rest.vm;

import cn.com.tiza.service.dto.ServiceStatus;
import lombok.Data;

/**
 * @author tz0920
 */
@Data
public class VehicleServiceInfoVM {
    private Long id;
    private Long serviceStartDate;
    private Long serviceEndDate;
    private Integer servicePeriod;
    private Integer serviceStatus;
}
