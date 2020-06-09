package cn.com.tiza.web.rest.vm;


import lombok.Data;

/**
 * gen by beetlsql 2019-02-21
 *
 * @author villas
 */
@Data
public class VehicleVM {

    private Long id;

    private Long jobId;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Long createAt;
    /**
     * 所属机构
     */
    private Long orgId;
    /**
     * 车牌号
     */
    private String plateNumber;
    /**
     * 终端编码
     */
    private String tid;
    /**
     * 更新时间
     */
    private Long updateAt;
    /**
     * VIN码
     */
    private String vin;

    private String plateCode;

    private String orderNo;

    private String orgName;

    private String province;

    private String city;

}
