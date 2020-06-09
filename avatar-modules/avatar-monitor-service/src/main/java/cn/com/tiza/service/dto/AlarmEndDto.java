package cn.com.tiza.service.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

/**
 * @author tz0920
 */
@Data
public class AlarmEndDto {

    @NotEmpty
    private String vin;
    private Long beginTime;
    private Long endTime;
    private String alarmItem;
    private Integer duration;
}
