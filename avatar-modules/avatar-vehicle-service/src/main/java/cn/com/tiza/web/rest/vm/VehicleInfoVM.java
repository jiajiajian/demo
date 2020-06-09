package cn.com.tiza.web.rest.vm;

import lombok.Data;

/**
 * 车辆信息
 * @author tiza
 */
@Data
public class VehicleInfoVM {
    private Long vehicleTypeId;
    private Long vehicleModelId;
    private Double totalWorkTime;
    private String vin;
}
