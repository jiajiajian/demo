package cn.com.tiza.web.rest.vm;

import lombok.Data;

import java.util.List;

/**
 * 统计分析
 */
@Data
public class VehicleAnalyseVM {
    private String province;
    private String city;
    private Integer count;
    private String vehicleTypeName;
    private Double totalWorkTime;
    private List<VehicleAnalyseVM> cityList;

    /**
     * 车辆区域统计导出
     * @return 字符串数组
     */
    public String[] toCountByProvinceRow() {
        String[] arr = new String[16];
        int idx = 0;
        arr[idx++] = province;
        arr[idx++] = count+"";
        return arr;
    }
    /**
     * 机型统计导出
     * @return 字符串数组
     */
    public String[] toCountByVehicleTypeRow() {
        String[] arr = new String[16];
        int idx = 0;
        arr[idx++] = vehicleTypeName;
        arr[idx++] = count+"";
        return arr;
    }
    /**
     * 工作小时统计导出
     * @return 字符串数组
     */
    public String[] toTotalWorkTimeByProvinceRow() {
        String[] arr = new String[16];
        int idx = 0;
        arr[idx++] = province;
        arr[idx++] = totalWorkTime+"";
        return arr;
    }
}
