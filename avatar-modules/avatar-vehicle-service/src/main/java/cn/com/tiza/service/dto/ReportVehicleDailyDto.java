package cn.com.tiza.service.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ReportVehicleDailyDto {

    @NotNull
    private Long startTime;

    @NotNull
    private Long endTime;
}
