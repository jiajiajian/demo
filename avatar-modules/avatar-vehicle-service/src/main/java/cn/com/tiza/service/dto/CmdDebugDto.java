package cn.com.tiza.service.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Data
public class CmdDebugDto {
    /**
     * 配置指令
     */
    private List<String> cmdList;
    /**
     * 机构ID
     */
    @NotNull
    private Long organizationId;
    /**
     * 备注
     */
    private String remark;
    /**
     * 类型ID
     */
    @NotNull
    private Long vehicleTypeId;

    public CmdDebugDto() {
    }
}
