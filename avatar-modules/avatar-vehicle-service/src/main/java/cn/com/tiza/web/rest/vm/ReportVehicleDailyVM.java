package cn.com.tiza.web.rest.vm;

import lombok.Data;

@Data
public class ReportVehicleDailyVM {
    private Long id;
    /**
     * 最后当前平均油耗
     */
    private Double endDfConsumption;
    /**
     * 最后燃油油位
     */
    private Double endFuelLevel;
    /**
     * 最后平均油耗
     */
    private Double endLfConsumption;
    /**
     * 发动机未运转时长（毫秒）
     */
    private Double engineNotRunTime;
    /**
     * 发动机运转时长（毫秒）
     */
    private Double engineRunTime;
    /**
     * 锤击时长（毫秒）
     */
    private Double hammerTime;
    /**
     * 最大冷却液温度
     */
    private Double maxCoolantTemp;
    /**
     * 最大进气管温度
     */
    private Double maxEimTemp;
    /**
     * 最大液压油温度
     */
    private Double maxHOilTemp;
    /**
     * 当日工作时间（毫秒）
     */
    private Double totalWorkingTime;
    private String vin;
    private String typeName;
    private String modelName;
    private String orgName;
    private String hammerRate;

    public String[] toRow(){
        String[] arr = new String[28];
        int idx = 0;
        arr[idx] = vin;
        arr[++idx] = orgName;
        arr[++idx] = typeName;
        arr[++idx] = modelName;
        arr[++idx] = totalWorkingTime+"";
        arr[++idx] = hammerTime+"";
        arr[++idx] = hammerRate;
        arr[++idx] = "" + engineRunTime;
        arr[++idx] = engineNotRunTime+"";
        arr[++idx] = maxCoolantTemp+"";
        arr[++idx] = maxHOilTemp+"";
        arr[++idx] = maxEimTemp+"";
        arr[++idx] = endLfConsumption+"";
        arr[++idx] = endDfConsumption+"";
        arr[++idx] = endFuelLevel+"";
        return arr;
    }
}
