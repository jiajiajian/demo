package cn.com.tiza.service.dto;

import lombok.Data;

@Data
public class WorkTimeDto {
    /**
     * 车辆类型
     */
    private Long vehicleTypeId;
    /**
     * 车辆型号
     */
    private Long vehicleModelId;
    /**
     * 机构
     */
    private Long orgId;
}
