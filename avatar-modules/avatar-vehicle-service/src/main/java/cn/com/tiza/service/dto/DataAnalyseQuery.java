package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Data;

/**
 * @author tiza
 */
@Data
public class DataAnalyseQuery extends Query {
    private Long organizationId;
    private String province;
    private Long vehicleTypeId;
    private Long vehicleModelId;
    private Long beginTime;
    private Long endTime;
    private String vin;
    private Integer hours;

    @Override
    protected void convertParams() {
        add("organizationId",this.organizationId);
        add("province",this.province);
        add("vehicleModelId",this.vehicleModelId);
        add("province",this.province);
        add("vehicleTypeId",this.vehicleTypeId);
        add("vehicleModelId",this.vehicleModelId);
        add("beginTime",this.beginTime);
        add("endTime",this.endTime);
        add("vin",vin);
        add("hours",this.hours);
    }
}
