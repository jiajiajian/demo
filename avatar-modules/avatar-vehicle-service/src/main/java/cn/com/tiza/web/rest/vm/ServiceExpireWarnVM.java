package cn.com.tiza.web.rest.vm;

import lombok.Data;

/**
 * @author tz0920
 */
@Data
public class ServiceExpireWarnVM {
    private String vin;
    private String simCard;
    private String terminalCode;
    private String serviceEndDate;
    private String orgName;

    public String[] toRow() {
        String[] arr = new String[5];
        int idx = 0;
        arr[idx] = vin;
        idx++;
        arr[idx] = terminalCode;
        idx++;
        arr[idx] = simCard;
        idx++;
        arr[idx] = serviceEndDate;
        idx++;
        arr[idx] = orgName;
        return arr;
    }
}
