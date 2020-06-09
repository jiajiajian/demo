package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Getter
@Setter
public class DashBoardQuery extends Query {

    private Long organizationId;
    private Long vehicleTypeId;
    private Long vehicleModelId;
    private String province;
    private String city;
    private String area;
    private Integer lock;
    private Integer acc;
    private String customerName;
    private String vin;
    /**
     * 批量查询vinList
     */
    private List<String> vinList;

    @Override
    protected void convertParams() {
        add("organizationId",this.organizationId);
        add("vehicleTypeId",this.vehicleTypeId);
        add("vehicleModelId",this.vehicleModelId);
        add("province",this.province);
        add("city",this.city);
        add("area",this.area);
        add("lock",this.lock);
        add("acc",this.acc);
        add("customerName",this.customerName,true);
        add("vin",this.vin,true);
        add("vinList", this.vinList);
    }
}
