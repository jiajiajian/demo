package cn.com.tiza.web.rest.vm;


import lombok.Data;

/**
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Data
public class VehicleVM {
    /**
     * 机器序列号
     */
    private String vin;

    /**
     * 终端
     */
    private String terminalCode;
    /**
     * sim卡
     */
    private String simCode;
    private String orgName;
    /**
     * 车型
     */
    private String vehicleModel;
    /**
     * 车辆类型
     */
    private Long vehicleTypeId;
    private String vehicleType;


    public VehicleVM() {
    }


}
