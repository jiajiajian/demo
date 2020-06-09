package cn.com.tiza.service.dto;

import lombok.Data;


/**
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Data
public class VehicleSaleDto {
    private Long financeId;
    private Integer saleStatus;
    private Integer saleMethod;
    /**
     * 合同号
     */
    private String contractNumber;
    /**
     * 销售日期
     */
    private Long saleDate;
    /**
     * 按揭年限
     */
    private Integer loanPeriod;
    /**
     * 销售员
     */
    private String seller;
    /**
     * 客户ID
     */
    private Long customerId;

    public VehicleSaleDto() {
    }
}
