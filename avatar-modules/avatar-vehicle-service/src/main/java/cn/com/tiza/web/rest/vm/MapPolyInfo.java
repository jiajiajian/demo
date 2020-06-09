package cn.com.tiza.web.rest.vm;

import lombok.Data;

/**
 * @author tiza
 */
@Data
public class MapPolyInfo {
    private String vin;
    private Double lon;
    private Double lat;
    private Integer onlineStatus;
    private Integer alarmStatus;
    private Long dataUpdateTime;
}
