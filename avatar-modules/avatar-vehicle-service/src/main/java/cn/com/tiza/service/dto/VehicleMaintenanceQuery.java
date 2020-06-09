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
public class VehicleMaintenanceQuery extends Query {
    private Long organizationId;
    private String itemName;

    @Override
    protected void convertParams() {
        add("organizationId", this.organizationId);
        add("itemName", this.itemName, true);
    }
}
