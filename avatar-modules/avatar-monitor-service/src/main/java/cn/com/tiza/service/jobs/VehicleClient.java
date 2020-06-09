package cn.com.tiza.service.jobs;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author tz0920
 */
@FeignClient("feisi-vehicle")
public interface VehicleClient {
    /**
     * 车辆服务过期定时任务
     */
    @GetMapping("/vehicle/vehicleServiceExpireJob")
    void vehicleServiceExpireJob();

    /**
     * 车辆调试状态定时任务
     */
    @GetMapping("/vehicleDebug/vehicleTestServiceJob")
    void vehicleTestServiceJob();

    /**
     * 获取车辆根组织id
     *
     * @param vin vin
     * @return
     */
    @GetMapping("/vehicle/rootOrgByVin/{vin}")
    Long rootOrgByVin(@PathVariable("vin") String vin);

    /**
     * 定时任务生成保养记录
     */
    @GetMapping("/maintenanceLog/produceMaintenanceLog")
    void produceMaintenanceLog();
}
