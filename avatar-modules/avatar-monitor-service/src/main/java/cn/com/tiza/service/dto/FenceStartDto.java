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
public class FenceStartDto {

    @NotEmpty
    private String vin;
    private Integer orgType;
    private Long organizationId;
    @NotEmpty
    private Long beginTime;
    @NotEmpty
    private AlarmType alarmType;
    private Long fenceId;
    private String lon;
    private String lat;
    private String province;
    private String city;
    private String area;

}
