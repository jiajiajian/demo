package cn.com.tiza.web.rest.vm;


import lombok.Data;

import java.math.BigDecimal;

/**
 * gen by beetlsql 2020-04-14
 *
 * @author tiza
 */
@Data
public class VehicleRealtimeVM extends LockVM {

    /**
     * 主键ID
     */
    private Long id;
    /**
     *  在线状态 0:离线 1:在线
     */
    private Integer onlineStatus;
    /**
     * ACC状态（0：ACC关，1：ACC开）
     */
    private Integer acc;
    /**
     * 故障状态（0：无故障 1：有故障）
     */
    private Integer faultStatus;
    /**
     * 定位状态（0：GPS不定位，1：GPS已定位）
     */
    private Integer gps;
    /**
     * 数据更新时间
     */
    private Long dataUpdateTime;
    /**
     * 调试结束时间
     */
    private Long debugEndTime;
    /**
     * 调试开始时间
     */
    private Long debugStartTime;
    /**
     * 备注
     */
    private String description;
    /**
     * GPS位置
     */
    private String gpsAddress;
    /**
     * 当前区县
     */
    private String gpsArea;
    /**
     * 当前市
     */
    private String gpsCity;
    /**
     * 当前省
     */
    private String gpsProvince;
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
     * gps纬度
     */
    private BigDecimal lat;
    /**
     * gps经度
     */
    private BigDecimal lon;
    /**
     * 累计工作时间
     */
    private Long totalWorkTime;
    /**
     * 机器序列号
     */
    private String vin;
    /**
     * 信号强度
     */
    private String signalStrength;

    public VehicleRealtimeVM() {
    }


}
