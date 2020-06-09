package cn.com.tiza.service.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;


/**
 * 故障结束DTO
 *
 * @author
 */
@Data
public class FaultEndDto {
    @NotEmpty
    private String vin;
    @NotEmpty
    private Long endTime;
    private Long beginTime;
    private String spnFmi;
    private Integer duration;
}
