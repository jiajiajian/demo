package cn.com.tiza.service.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * gen by beetlsql 2020-03-09
 *
 * @author tiza
 */
@Data
public class FunctionSetItemDto {

    /**
     * 位长度
     */
    @NotNull
    private Integer bitLength;
    /**
     * 开始位
     */
    @NotNull
    private Integer bitStart;

    /**
     * 编号
     */
    @NotBlank
    private String code;
    /**
     * Int/String
     */
    private String dataFormat;
    /**
     * 英文名称
     */
    @NotBlank
    private String enName;
    /**
     * 功能集ID
     */
    private Long functionId;
    /**
     * 名称
     */
    @NotBlank
    private String name;
    /**
     * 偏移量
     */
    private String offset;
    /**
     * 系数
     */
    private String rate;
    /**
     * 单位
     */
    private String unit;

    private String description;

    @NotBlank
    private String varAddress;

    private String separator;

    private Integer sortNum;

    public FunctionSetItemDto() {
    }

}
