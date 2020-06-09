package cn.com.tiza.web.rest.vm;


import lombok.Data;

/**
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Data
public class FaultDictItemVM {

    /**
     * 主键ID
     */
    private Long id;

    private String fmiName;
    private String orgName;
    /**
     * 故障码
     */
    private String spnFmi;
    private String spnName;
    private String tla;

    public FaultDictItemVM() {
    }

    public String[] toRow() {
        String[] arr = new String[5];
        int idx = 0;
        arr[idx++] = orgName;
        arr[idx++] = tla;
        arr[idx++] = spnFmi;
        arr[idx++] = spnName;
        arr[idx++] = fmiName;
        return arr;
    }
}
