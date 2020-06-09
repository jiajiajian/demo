package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Getter;
import lombok.Setter;

/**
 * gen by beetlsql 2020-03-23
 *
 * @author tiza
 */
@Getter
@Setter
public class FenceAlarmQuery extends Query {

    private String code;
    private String vin;
    private Long vehicleTypeId;
    private Long vehicleModelId;
    private Integer fenceType;
    private Integer alarmType;
    private Integer alarmState;
    private Long endTime;
    private Long beginTime;
    @Override
    protected void convertParams() {
        add("code", this.code, true);
        add("vin", this.vin);
        add("alarmState", this.alarmState);
        add("beginTime", this.beginTime);
        add("endTime", this.endTime);
        add("vehicleTypeId", this.vehicleTypeId);
        add("vehicleModelId", this.vehicleModelId);
        add("fenceType", this.fenceType);
        add("alarmType", this.alarmType);
        add("alarmState", this.alarmState);
    }

}
