package cn.com.tiza.web.rest.vm;


import cn.com.tiza.service.dto.FenceType;
import cn.com.tiza.util.LocalDateTimeUtils;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * gen by beetlsql 2020-03-31
 *
 * @author tiza
 */
@Data
public class AlarmHistoryVM {

    private Long id;
    private String vin;
    private String terminalCode;
    private String simCard;
    /**
     * 报警种类数量
     */
    private int itemNum;
    private int spnFmiNum;
    /**
     * 报警状态(0:未解除 1:已解除)
     */
    private Integer alarmState;
    /**
     * 聚合列表为车辆最新位置 详情页面为报警发生地
     */
    private String address;
    /**
     * 报警项名称
     */
    private String alarmName;
    /**
     * 故障码
     */
    private String faultCode;
    private String spnName;
    private String spn;
    private String fmi;
    private String fmiName;
    /**
     * 开始时间
     */
    private Long beginTime;
    /**
     * 持续时长
     */
    private BigDecimal duration;
    /**
     * 解除时间
     */
    private Long endTime;
    /**
     * FMI spn
     */
    private String spnFmi;
    private String tla;
    /**
     * 故障次数
     */
    private int frequency;
    /**
     * 最新时间
     */
    private Long dataUpdateTime;

    private BigDecimal lastLon;
    private BigDecimal lastLat;

    private Integer alarmType;

    private String lat ;
    private String lon ;

    private String alarmCode;
    private String orgName;
    private int mtuCount;
    private String recentlyCondition;

    public AlarmHistoryVM() {
    }

    public String[] toAlarmRow() {
        String[] arr = new String[6];
        int idx = 0;
        arr[idx++] = vin;
        arr[idx++] = terminalCode;
        arr[idx++] = simCard;
        arr[idx++] = String.valueOf(itemNum);
        arr[idx++] = dataUpdateTime == null ? null : LocalDateTimeUtils.formatDay(dataUpdateTime, LocalDateTimeUtils.DATE_TIME_PATTERN);
        arr[idx++] = address;
        return arr;
    }

    public String[] toSingleVehicleAlarmRow() {
        String[] arr = new String[4];
        int idx = 0;
        arr[idx++] = beginTime == null ? null : LocalDateTimeUtils.formatDay(beginTime, LocalDateTimeUtils.DATE_TIME_PATTERN);
        arr[idx++] = endTime == null ? null : LocalDateTimeUtils.formatDay(endTime, LocalDateTimeUtils.DATE_TIME_PATTERN);
        arr[idx++] = alarmName;
        arr[idx++] = address;
        return arr;
    }

    public String[] toFaultRow() {
        String[] arr = new String[7];
        int idx = 0;
        arr[idx++] = vin;
        arr[idx++] = terminalCode;
        arr[idx++] = simCard;
        arr[idx++] = String.valueOf(spnFmiNum) ;
        arr[idx++] = String.valueOf(mtuCount) ;
        arr[idx++] = dataUpdateTime == null ? null : LocalDateTimeUtils.formatDay(dataUpdateTime, LocalDateTimeUtils.DATE_TIME_PATTERN);
        arr[idx++] = address;
        return arr;
    }

    public String[] toSingleVehicleFaultRow() {
        String[] arr = new String[8];
        int idx = 0;
        arr[idx++] = beginTime == null ? null : LocalDateTimeUtils.formatDay(beginTime, LocalDateTimeUtils.DATE_TIME_PATTERN);
        arr[idx++] = endTime == null ? null : LocalDateTimeUtils.formatDay(endTime, LocalDateTimeUtils.DATE_TIME_PATTERN);
        arr[idx++] = spnFmi;
        arr[idx++] = tla;
        arr[idx++] = spnName;
        arr[idx++] = fmiName;
        arr[idx++] = String.valueOf(frequency) ;
        arr[idx++] = address;
        return arr;
    }

    public String[] toSingleVehicleFaultRow1() {
        String[] arr = new String[6];
        int idx = 0;
        arr[idx++] = beginTime == null ? null : LocalDateTimeUtils.formatDay(beginTime, LocalDateTimeUtils.DATE_TIME_PATTERN);
        arr[idx++] = endTime == null ? null : LocalDateTimeUtils.formatDay(endTime, LocalDateTimeUtils.DATE_TIME_PATTERN);
        arr[idx++] = spnFmi;
        arr[idx++] = spnName;
        arr[idx++] = String.valueOf(frequency) ;
        arr[idx++] = address;
        return arr;
    }

}
