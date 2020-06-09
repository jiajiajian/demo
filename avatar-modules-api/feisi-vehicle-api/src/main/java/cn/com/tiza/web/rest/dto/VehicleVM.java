package cn.com.tiza.web.rest.dto;


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
    private String financeName;
    /**
     * 车型ID
     */
    private Long vehicleModelId;
    private String vehicleModel;
    /**
     * 车辆类型
     */
    private Long vehicleTypeId;
    private String vehicleType;

    /**
     * 按揭年限
     */
    private Integer loanPeriod;
    /**
     * 销售日期yyyyMMdd
     */
    private Integer saleDate;
    /**
     * 销售方式
     */
    private Integer saleMethod;
    /**
     * 销售状态
     */
    private Integer saleStatus;
    /**
     * 销售员
     */
    private String seller;

    public VehicleVM() {
    }
}
