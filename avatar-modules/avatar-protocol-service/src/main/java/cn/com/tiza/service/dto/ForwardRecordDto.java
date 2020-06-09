package cn.com.tiza.service.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author villas
 * @since 2019/5/30 14:53
 */
@Data
public class ForwardRecordDto {
    private String name;

    private Long createAt;

    private Long dataAmount;

    private Integer dataType;

    private Long jobId;

    private Long statisticalTime;

    private Long totalDataAmount;

    /**
     * Query command id, 0 means all
     */
    Integer cmdID;

    /**
     * Query start time stamp
     */
    @NotNull
    Long startTime;

    /**
     * Query end time stamp
     */
    @NotNull
    Long endTime;
}
