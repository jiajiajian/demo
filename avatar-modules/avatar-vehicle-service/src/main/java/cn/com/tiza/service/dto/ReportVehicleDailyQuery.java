package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * gen by beetlsql 2020-03-31
 *
 * @author tiza
 */
@Getter
@Setter
public class ReportVehicleDailyQuery extends Query {
    private Long beginTime;

    private Long endTime;

    private Long organizationId;

    private Long vehicleTypeId;

    private Long vehicleModelId;

    private String vin;

    @Override
    protected void convertParams() {
        add("beginTime", this.beginTime);
        add("vehicleTypeId", this.vehicleTypeId);
        add("vehicleModelId", this.vehicleModelId);
        add("vin", this.vin,true);
        add("endTime", this.endTime);
    }
}
