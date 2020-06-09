package cn.com.tiza.web.rest.vm;


import cn.com.tiza.service.dto.FenceType;
import cn.com.tiza.util.LocalDateTimeUtils;
import lombok.Data;

import java.math.BigDecimal;

/**
 * gen by beetlsql 2020-03-31
 *
 * @author tiza
 */
@Data
public class FenceAlarmVM {

    private Long id;
    private String vin;
    private String orgName;

    /**
     * 报警状态(0:未解除 1:已解除)
     */
    private Integer alarmState;

    private String address;

    private Integer fenceType;
    private Integer alarmType;
    /**
     * 开始时间
     */
    private Long beginTime;
    /**
     * 解除时间
     */
    private Long endTime;


    public FenceAlarmVM() {
    }
    public String[] toFenceRow() {
        String[] arr = new String[7];
        int idx = 0;
        arr[idx++] = vin;
        arr[idx++] = orgName;
        arr[idx++] = parseFenceType(fenceType);
        arr[idx++] = alarmType == 0 ? "出围栏报警" : "进围栏报警";
        arr[idx++] = address;
        arr[idx++] = beginTime == null ? null : LocalDateTimeUtils.formatDay(beginTime, LocalDateTimeUtils.DATE_TIME_PATTERN);
        arr[idx++] = endTime == null ? null : LocalDateTimeUtils.formatDay(endTime, LocalDateTimeUtils.DATE_TIME_PATTERN);
        return arr;
    }


    public String[] toSingleVehicleFenceRow() {
        String[] arr = new String[5];
        int idx = 0;
        arr[idx++] = beginTime == null ? null : LocalDateTimeUtils.formatDay(beginTime, LocalDateTimeUtils.DATE_TIME_PATTERN);
        arr[idx++] = endTime == null ? null : LocalDateTimeUtils.formatDay(endTime, LocalDateTimeUtils.DATE_TIME_PATTERN);
        arr[idx++] = parseFenceType(fenceType);
        arr[idx++] = alarmType == 0 ? "出围栏报警" : "进围栏报警";
        arr[idx++] = address;
        return arr;
    }

    private String parseFenceType(Integer fenceType) {
        FenceType fenceType1 = FenceType.fromValue(fenceType);
        return fenceType1.getName();
    }
}
