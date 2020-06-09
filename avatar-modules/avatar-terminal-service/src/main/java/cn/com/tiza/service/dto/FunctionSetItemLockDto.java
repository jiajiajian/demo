package cn.com.tiza.service.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * gen by beetlsql 2020-03-31
 *
 * @author tiza
 */
@Data
public class FunctionSetItemLockDto {

    /**
     * 中文名称
     */
    @NotBlank
    private String chinaName;
    /**
     * key标识
     */
    private String code;
    /**
     * 变量类型---对应字典表(item表)中的id
     */
    @NotNull
    private Long dicItemId;
    /**
     * 英文名称
     */
    @NotBlank
    private String englishName;

    /**
     * 功能集ID
     */
    private Long functionId;

    private String message;

    public FunctionSetItemLockDto() {
    }


}
