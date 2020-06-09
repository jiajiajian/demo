package cn.com.tiza.service.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Data
public class FaultDictItemDto {
    @NotNull
    private String fmi;
    private String fmiName;
    @NotNull
    private Long organizationId;
    private String rootOrgName;
    @NotNull
    private String spn;
    @NotNull
    private String spnName;
    private Long tlaId;
    private String tla;

}
