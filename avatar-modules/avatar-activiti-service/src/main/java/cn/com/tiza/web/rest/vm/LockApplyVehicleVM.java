package cn.com.tiza.web.rest.vm;

import lombok.Data;

@Data
public class LockApplyVehicleVM {

    private String applyCode;

    private String terminalCode;

    private String simCard;

    private String orgName;

    private String vehicleType;

    private String vehicleModel;
}
