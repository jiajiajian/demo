package cn.com.tiza.web.rest.vm;

import lombok.Data;

/**
 * @author tz0920
 */
@Data
public class ActiveNumDistributeVM {
    private String province;
    private int t10;
    private int t20;
    private int t30;
    private int t40;

    public String[] toRow() {
        String[] arr = new String[5];
        int idx = 0;
        arr[idx] = province;
        ++idx;
        arr[idx] = String.valueOf(t10);
        ++idx;
        arr[idx] = String.valueOf(t20);
        ++idx;
        arr[idx] = String.valueOf(t30);
        ++idx;
        arr[idx] = String.valueOf(t40);
        return arr;
    }
}
