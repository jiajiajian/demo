package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
* gen by beetlsql 2020-03-17
* @author tiza
*/
@Getter
@Setter
public class VehicleDebugQuery extends Query {

     private Integer status;
     private String vin;
     private String vehicleTypeId;
     private String vehicleModelId;
     private List<String> vinList;

    @Override
    protected void convertParams() {
         add("status", this.status);
         add("vin",this.vin,true);
         add("vehicleModelId",vehicleModelId);
        add("vehicleTypeId",vehicleTypeId);
        add("vinList",vinList);
    }

}
