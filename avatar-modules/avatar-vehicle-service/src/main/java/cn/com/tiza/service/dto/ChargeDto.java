package cn.com.tiza.service.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * gen by beetlsql 2020-04-20
 *
 * @author tiza
 */
@Data
public class ChargeDto {

    private Long id;
    /**
     * 配置状态 0:未配置 1:已配置
     */
    private Integer configFlag;
    /**
     * 组织id
     */
    @NotNull
    private Long organizationId;
    /**
     * 终端型号
     */
    @NotNull
    private String terminalModel;

    public ChargeDto() {
    }


}
