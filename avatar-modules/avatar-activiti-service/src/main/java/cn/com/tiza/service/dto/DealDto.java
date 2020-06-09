package cn.com.tiza.service.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DealDto {

    /** 处理意见 */
    @NotNull
    private String comment;

    /** 执行操作 */
    @NotNull
    private Integer action;
}
