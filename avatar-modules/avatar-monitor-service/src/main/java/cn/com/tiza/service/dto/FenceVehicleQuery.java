package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Getter;
import lombok.Setter;

/**
 * gen by beetlsql 2020-03-31
 *
 * @author tiza
 */
@Getter
@Setter
public class FenceVehicleQuery extends Query {
    private String code;
    private Long vehicleModelId;
    private Long vehicleTypeId;
    private Long financeId;
    private Long fenceId;

    @Override
    protected void convertParams() {
        add("code", this.code, true);
        add("vehicleModelId", this.vehicleModelId);
        add("vehicleTypeId", this.vehicleTypeId);
        add("financeId", this.financeId);
        add("fenceId", this.fenceId);
    }

}
