package cn.com.tiza.web.rest.vm;

import lombok.Data;

/**
 * @author tz0920
 */
@Data
public class VehicleLockItemVM {
    private String vin;
    private Long softVersionId;
    private Long functionId;
    /**
     * 锁类型对应字典表code
     */
    private String itemCode;
    private String itemName;
}
