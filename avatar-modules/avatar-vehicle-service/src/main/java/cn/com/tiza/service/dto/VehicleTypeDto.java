package cn.com.tiza.service.dto;

import lombok.Data;


/**
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Data
public class VehicleTypeDto {
    /**
     * 其他
     */
    private String description;
    /**
     * 类型名称
     */
    private String name;
    /**
     * 机构ID
     */
    private Long organizationId;

    public VehicleTypeDto() {
    }


}
