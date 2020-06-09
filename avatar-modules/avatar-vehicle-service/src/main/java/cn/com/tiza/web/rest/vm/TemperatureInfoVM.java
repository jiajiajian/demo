package cn.com.tiza.web.rest.vm;

import lombok.Data;

import java.util.List;

@Data
public class TemperatureInfoVM {
    //冷却液温度
    private List<DailyTemperature> coolantTempList;
    //液压油温度
    private List<DailyTemperature> hydraulicOilTempList;
    //燃油温度
    private List<DailyTemperature> fuelTempList;
}
