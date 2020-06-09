package cn.com.tiza.service.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * 车辆服务dto
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Data
public class VehicleServiceDto {
    /**
     * 服务结束时间y
     */
    @NotNull
    private Long serviceEndDate;
    /**
     * 服务期限
     */
    @NotNull
    private Integer servicePeriod;
    /**
     * 服务开始时间
     */
    @NotNull
    private Long serviceStartDate;
    /**
     * 服务状态
     */
    @NotNull
    private Integer serviceStatus;

    public VehicleServiceDto() {
    }
}
