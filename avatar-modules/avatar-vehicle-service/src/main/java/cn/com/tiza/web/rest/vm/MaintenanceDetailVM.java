package cn.com.tiza.web.rest.vm;

import lombok.Data;

import java.util.List;
@Data
public class MaintenanceDetailVM {
    private Integer type;
    private Integer hours;
    private List<MaintenanceInfoVM> items;
}
