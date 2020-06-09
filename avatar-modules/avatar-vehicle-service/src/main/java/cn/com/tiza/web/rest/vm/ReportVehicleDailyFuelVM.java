package cn.com.tiza.web.rest.vm;

import lombok.Data;

/**
 * @author villas
 */
@Data
public class ReportVehicleDailyFuelVM {

    private Integer time;

    /**
     * 平均油耗
     */
    private Double avgFuelUse;

    /**
     * 当前平均油耗
     */
    private Double curAvgFuelUse;

    /**
     * 燃油油位
     */
    private Double fuelLevel;
}
