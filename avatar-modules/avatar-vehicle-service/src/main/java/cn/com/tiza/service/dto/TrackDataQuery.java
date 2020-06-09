package cn.com.tiza.service.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author tz0920
 */
@Data
public class TrackDataQuery {
    @NotNull
    private Long beginTime;
    @NotNull
    private Long endTime;
    private Integer page;
    private Integer pageSize;
}
