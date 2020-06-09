package cn.com.tiza.service.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author tz0920
 */
@Data
public class TonnageMonthVehicleNumQuery {
    private Long organizationId;

    @NotNull
    private Integer tonnage;

    @NotNull
    private Integer beginMon;

    @NotNull
    private Integer endMon;
}
