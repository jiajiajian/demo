package cn.com.tiza.web.rest.vm;


import cn.com.tiza.service.dto.ServiceStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * gen by beetlsql 2020-04-20
 *
 * @author tiza
 */
@Data
public class PrePaidVM {

    private String vin;
    private String simCard;
    private String terminalModel;
    private String serviceStartDate;
    private String serviceEndDate;
    /**
     * 本月入网：服务期限 本月续费：续费时长
     */
    private Integer servicePeriod;
    private Integer serviceStatus;
    /**
     * 代理商
     */
    private String agent;
    /**
     * 总部
     */
    private String rootOrg;
    private Long rootOrgId;
    /**
     * 1:新入网 2:续费
     */
    private Integer flag;
    /**
     * 是否收费
     */
    private boolean fee;
    /**
     * 信息服务费
     */
    private BigDecimal serviceFee;
    /**
     * 是否续费
     */
    private boolean renew;
    /**
     * 信息服务费
     */
    private BigDecimal renewFee;


    public PrePaidVM() {
    }

    public String[] toRow() {
        String[] arr = new String[14];
        int idx = 0;
        arr[idx] = vin;
        idx++;
        arr[idx] = simCard;
        idx++;
        arr[idx] = serviceStartDate;
        idx++;
        arr[idx] = String.valueOf(servicePeriod);
        idx++;
        arr[idx] = serviceEndDate;
        idx++;
        arr[idx] = "已开通";
        idx++;
        arr[idx] = agent;
        idx++;
        arr[idx] = rootOrg;
        idx++;
        arr[idx] = flag == 1 && Objects.nonNull(serviceFee) ? "是" : "否";
        idx++;
        arr[idx] = flag == 1 && Objects.nonNull(serviceFee) ? serviceFee.toString() : "";
        idx++;
        arr[idx] = flag == 1 ? "是" :"否";
        idx++;
        arr[idx] = flag == 2 ? "是" :"否";
        idx++;
        arr[idx] = flag == 2 && Objects.nonNull(servicePeriod) ? String.valueOf(servicePeriod) : "";
        idx++;
        arr[idx] = flag == 2 && Objects.nonNull(serviceFee) ? serviceFee.toString() : "";
        return arr;
    }

}
