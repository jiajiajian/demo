package cn.com.tiza.web.rest.vm;

import lombok.Data;

/**
 * @author tiza
 */
@Data
public class MachineWorkDetailVM {
    private String beginTime;
    private String endTime;
    private Double workTime;

    /**
     * 详细工作时间统计导出
     * @return 字符串数组
     */
    public String[] toRow() {
        String[] arr = new String[16];
        int idx = 0;
        arr[idx++] = beginTime;
        arr[idx++] = endTime;
        arr[idx++] = workTime+"";
        return arr;
    }
}
