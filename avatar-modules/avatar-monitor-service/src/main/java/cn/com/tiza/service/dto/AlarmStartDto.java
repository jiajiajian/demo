package cn.com.tiza.service.dto;

import cn.com.tiza.dto.AlarmType;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author tz0920
 */
@Data
public class AlarmStartDto {

    @NotEmpty
    private String vin;
    private Long organizationId;
    @NotEmpty
    private Long beginTime;
    private AlarmType alarmType;
    private String alarmItem;
    private String lon;
    private String lat;
    private String province;
    private String city;
    private String area;
}
