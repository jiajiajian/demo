package cn.com.tiza.web.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 车辆服务接口
 * @author tiza
 */
@FeignClient(value = "FEISI-VEHICLE", fallback = HystrixVehicleApiCallback.class)
public interface VehicleApiClient {

    /**
     * 查询组织下车辆数
     * @param orgId 组织id
     * @return 车辆数
     */
    @GetMapping("/vehicle/relate/org/{orgId}")
    Long countVehicleByOrg(@PathVariable("orgId") Long orgId);

}

