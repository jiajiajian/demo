package cn.com.tiza.service.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;


/**
 * 故障结束DTO
 *
 * @author
 */
@Data
public class FenceEndDto {
    @NotEmpty
    private String vin;
    @NotEmpty
    private Long endTime;
    private Long beginTime;
    private Long fenceId;
    private Integer duration;
}
