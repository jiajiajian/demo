package cn.com.tiza.web.rest.vm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class AvgModelOilConsumptionVM {
    @JsonIgnore
    private String vin;
    @JsonIgnore
    private String code;
    private String vehicleModel;

    /**
     * 车型油耗平均值
     */
    private Double modelAvgOilConsume;

    /**
     * 车辆数
     */
    private int num;

    /**
     * 车型油耗最大值
     */
    private Double modelMaxOilConsume;

    /**
     * 车型油耗最小值
     */
    private Double modelMinOilConsume;



    public String[] toRow() {
        String[] arr = new String[6];
        int idx = 0;
        arr[idx] = vehicleModel ;
        ++idx;
        arr[idx] = String.valueOf(num);
        ++idx;
        arr[idx] = String.valueOf(modelAvgOilConsume);
        ++idx;
        arr[idx] = vehicleModel;
        ++idx;
        arr[idx] = String.valueOf(modelMaxOilConsume) ;
        ++idx;
        arr[idx] = String.valueOf(modelMinOilConsume) ;
        return arr;
    }

}
