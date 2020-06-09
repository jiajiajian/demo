package cn.com.tiza.service.dto;

import cn.com.tiza.dto.AlarmType;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 故障开始DTO
 *
 * @author TZ0920
 */
@Data
public class FaultStartDto {

    @NotEmpty
    private String vin;
    private Long organizationId;
    @NotEmpty
    private Long beginTime;
    @NotEmpty
    private AlarmType alarmType;
    private String lon;
    private String lat;
    private Long tlaId;
    private String tla;
    private String spnFmi;
    private int frequency;
    private String province;
    private String city;
    private String area;

}
