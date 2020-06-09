package cn.com.tiza.web.rest.vm;


import lombok.Data;

import java.math.BigDecimal;

/**
 * gen by beetlsql 2020-04-14
 *
 * @author tiza
 */
@Data
public class FrameVehicleVM {

    /**
     * ACC状态（0：ACC关，1：ACC开）
     */
    private Integer acc;
    private String vehicleType;
    private String vehicleModel;
    private String terminalCode;
    private String simCard;
    /**
     * GPS位置
     */
    private String gpsAddress;
    /**
     * 定位有效时间
     */
    private Long gpsTime;
    /**
     * 最新数据纬度
     */
    private BigDecimal lastLat;
    /**
     * 最新数据经度
     */
    private BigDecimal lastLon;
    /**
     * 累计工作时间 s
     */
    private Long totalWorkTime;
    /**
     * h
     */
    private BigDecimal workTime;
    /**
     * 机器序列号
     */
    private String vin;

    public FrameVehicleVM() {
    }


}
