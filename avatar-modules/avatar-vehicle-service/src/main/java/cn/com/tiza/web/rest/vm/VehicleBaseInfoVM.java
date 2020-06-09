package cn.com.tiza.web.rest.vm;

import lombok.Data;

/**
 * @author tz0920
 */
@Data
public class VehicleBaseInfoVM {
    private String vin;
    private String terminalCode;
    private String simCard;
    private String orgName;
    private Long rootOrgId;
    private Boolean showTla;
}
