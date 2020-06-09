package cn.com.tiza.web.rest.vm;

import lombok.Data;

@Data
public class WorkTimeVM {
    private String vin;
    private String orgName;
    private String vehicleTypeName;
    private String vehicleModelName;
    private double totalWorkTime;
    private double workTime;
    private String updateTime;
    private String address;

    /**
     * 区域工作小时统计导出
     * @return 字符串数组
     */
    public String[] toRow() {
        String[] arr = new String[16];
        int idx = 0;
        arr[idx++] = vin;
        arr[idx++] = orgName;
        arr[idx++] = vehicleTypeName;
        arr[idx++] = vehicleModelName;
        arr[idx++] = totalWorkTime+"";
        arr[idx++] = workTime+"";
        arr[idx++] = updateTime;
        arr[idx++] = address;
        return arr;
    }
}
