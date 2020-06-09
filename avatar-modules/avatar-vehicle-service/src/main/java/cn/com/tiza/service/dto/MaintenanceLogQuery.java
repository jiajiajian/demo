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
public class MaintenanceLogQuery extends Query {

    private Long organizationId;
    private Long vehicleTypeId;
    private Long vehicleModelId;
    private String vin;
    private Integer handleStatus;

    @Override
    protected void convertParams() {
        add("organizationId", this.organizationId);
        add("vehicleTypeId", this.vehicleTypeId);
        add("vehicleModelId", this.vehicleModelId);
        add("vin", this.vin,true);
        add("handleStatus", this.handleStatus);
    }

}
