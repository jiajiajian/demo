package cn.com.tiza.service.dto;

import cn.com.tiza.dto.AlarmType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author 0920
 */
@Data
public class KafkaAlarmDto {

    @JsonProperty("VIN")
    private String vin;
    /**
     * 报警状态：0表示发生报警，1表示结束报警
     */
    @JsonProperty("ALARM_STATE")
    private AlarmState state;
    /**
     * 报警类型
     */
    @JsonProperty("TYPE")
    private Integer alarmType;
    /**
     * 机构类型 1:机构 2:融资机构
     */
    @JsonProperty("ORG_TYPE")
    private Integer orgType;
    /**
     * 报警：报警项code，故障：tla_id.SPN.FMI.故障次数   围栏：围栏id
     */
    @JsonProperty("ALARM_ITEM")
    private String alarmItem;

    /**
     * 开始时间
     */
    @JsonProperty("START_TIME")
    private Long startTime;

    /**
     * 结束时间
     */
    @JsonProperty("END_TIME")
    private Long endTime;

    /**
     * 持续时长 单位：ms
     */
    @JsonProperty("DURATION")
    private Integer duration;

    @JsonProperty("LON")
    private String lon;
    @JsonProperty("LAT")
    private String lat;
    @JsonProperty("PROVINCE")
    private String province;
    @JsonProperty("CITY")
    private String city;
    @JsonProperty("AREA")
    private String area;

    public AlarmStartDto asAlarmStart() {
        AlarmStartDto dto = new AlarmStartDto();
        dto.setVin(vin);
        dto.setAlarmItem(alarmItem);
        dto.setAlarmType(AlarmType.fromValue(alarmType));
        dto.setLon(lon);
        dto.setLat(lat);
        dto.setBeginTime(startTime);
        dto.setProvince(province);
        dto.setCity(city);
        dto.setArea(area);
        return dto;
    }

    public AlarmEndDto asAlarmEnd() {
        AlarmEndDto dto = new AlarmEndDto();
        dto.setVin(vin);
        dto.setAlarmItem(alarmItem);
        dto.setBeginTime(startTime);
        dto.setEndTime(endTime);
        dto.setDuration(duration);
        return dto;
    }

    public FaultStartDto asFaultStart() {
        FaultStartDto dto = new FaultStartDto();
        dto.setVin(vin);
        dto.setAlarmType(AlarmType.fromValue(alarmType));
        int last = alarmItem.lastIndexOf(".");
        int first = alarmItem.indexOf(".");
        dto.setTlaId(Long.parseLong(alarmItem.substring(0, first)));
        dto.setSpnFmi(alarmItem.substring(first + 1, last));
        dto.setFrequency(Integer.parseInt(alarmItem.substring(last + 1, alarmItem.length())));
        dto.setLon(lon);
        dto.setLat(lat);
        dto.setBeginTime(startTime);
        dto.setProvince(province);
        dto.setCity(city);
        dto.setArea(area);
        return dto;
    }

    public FaultEndDto asFaultEnd() {
        FaultEndDto dto = new FaultEndDto();
        dto.setVin(vin);
        dto.setBeginTime(startTime);
        dto.setEndTime(endTime);
        dto.setDuration(duration);
        int first = alarmItem.indexOf(".");
        dto.setSpnFmi(alarmItem.substring(first + 1, alarmItem.length()));
        return dto;
    }

    public FenceStartDto asFenceStart() {
        FenceStartDto dto = new FenceStartDto();
        dto.setVin(vin);
        dto.setOrgType(orgType);
        dto.setAlarmType(AlarmType.fromValue(alarmType));
        dto.setFenceId(Long.parseLong(alarmItem));
        dto.setLon(lon);
        dto.setLat(lat);
        dto.setBeginTime(startTime);
        dto.setProvince(province);
        dto.setCity(city);
        dto.setArea(area);
        return dto;
    }

    public FenceEndDto asFenceEnd() {
        FenceEndDto dto = new FenceEndDto();
        dto.setVin(vin);
        dto.setFenceId(Long.parseLong(alarmItem));
        dto.setBeginTime(startTime);
        dto.setEndTime(endTime);
        dto.setDuration(duration);
        return dto;
    }
}
