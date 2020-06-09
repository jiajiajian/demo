package cn.com.tiza.web.rest.vm;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author tz0920
 */
@Data
public class AfterPaidVM {
    private String vin;
    private Long rootOrgId;
    private String orgName;
    private String rootOrgName;
    private String simCard;
    private String terminalModel;
    private String registDate;
    private String serviceEndDate;
    private String serviceStartDate;
    /**
     * 已收费月份
     */
    private int alreadyChargedMon;
    /**
     * 本次收费月份
     */
    private int chargeMon;
    private BigDecimal chargeFee;
    private BigDecimal fee;
    /**
     * 未收费月份
     */
    private int notChargeMon;

    public String[] toRow() {
        String[] arr = new String[10];
        int idx = 0;
        arr[idx] = vin;
        idx++;
        arr[idx] = simCard;
        idx++;
        arr[idx] = rootOrgName;
        idx++;
        arr[idx] = registDate;
        idx++;
        arr[idx] = serviceEndDate;
        idx++;
        arr[idx] = "" + alreadyChargedMon;
        idx++;
        arr[idx] = "" + chargeMon;
        idx++;
        arr[idx] = "" + notChargeMon;
        idx++;
        arr[idx] = "" + 0;
        idx++;
        arr[idx] = null == chargeFee ? "" : chargeFee.toString();
        return arr;
    }
}
