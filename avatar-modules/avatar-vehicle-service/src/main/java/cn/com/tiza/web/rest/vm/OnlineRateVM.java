package cn.com.tiza.web.rest.vm;

import lombok.Data;

/**
 * 在线率统计
 * @author tiza
 */
@Data
public class OnlineRateVM {
    private Long onlineCount;
    private Long offlineCount;
    private String onlineRate;
    private String dateStr;

    /**
     * 区域工作小时统计导出
     * @return 字符串数组
     */
    public String[] toRow() {
        String[] arr = new String[16];
        int idx = 0;
        arr[idx++] = dateStr;
        arr[idx++] = onlineCount+"";
        arr[idx++] = offlineCount+"";
        arr[idx++] = onlineRate;
        return arr;
    }
}
