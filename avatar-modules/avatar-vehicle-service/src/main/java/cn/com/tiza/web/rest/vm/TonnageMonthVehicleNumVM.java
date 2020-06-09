package cn.com.tiza.web.rest.vm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class TonnageMonthVehicleNumVM {

    private String month;

    private Integer tonnage;

    /**
     * 0~30H 车辆数
     */
    private Integer less30;

    private Integer less30Per;

    /**
     * 30~240H 车辆数
     */
    private Integer between30To240;

    private Integer between30To240Per;

    /**
     * 240~500H 车辆数
     */
    private Integer between240To500;

    private Integer between240To500Per;

    /**
     * 》500H 车辆数
     */
    private Integer more500;

    private Integer more500Per;

    @JsonIgnore
    private Integer totalNum;

    public String[] toRow() {
        String[] arr = new String[6];
        int idx = 0;
        arr[idx] = new StringBuilder().append(String.valueOf(month), 0, 4).append("-").append(String.valueOf(month), 4,6).toString() ;
        ++idx;
        arr[idx] = String.valueOf(tonnage);
        ++idx;
        arr[idx] = String.valueOf(less30);
        ++idx;
        arr[idx] = String.valueOf(between30To240);;
        ++idx;
        arr[idx] = String.valueOf(between240To500) ;
        ++idx;
        arr[idx] = String.valueOf(more500) ;
        return arr;
    }
}
