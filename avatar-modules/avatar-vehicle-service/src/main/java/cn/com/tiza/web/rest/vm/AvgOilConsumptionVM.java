package cn.com.tiza.web.rest.vm;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author tz0920
 */
@Data
public class AvgOilConsumptionVM {
    private String vin;
    private String code;
    private String terminalCode;
    private String vehicleType;
    private String vehicleModel;
    private Double vehicleAvgOilConsume;
    private Double modelAvgOilConsume;
    private BigDecimal percent;


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
        arr[idx] = vin ;
        ++idx;
        arr[idx] = terminalCode;
        ++idx;
        arr[idx] = vehicleType;
        ++idx;
        arr[idx] = vehicleModel;
        ++idx;
        arr[idx] = String.valueOf(vehicleAvgOilConsume) ;
        ++idx;
        arr[idx] = String.valueOf(modelAvgOilConsume) ;
        return arr;
    }
}
