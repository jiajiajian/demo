package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author tz0920
 */
@Data
public class AvgOilConsumptionQuery{
    @NotNull
    private Long vehicleTypeId;
    @NotNull
    private Long vehicleModelId;
    @NotNull
    private Long organizationId;
}
