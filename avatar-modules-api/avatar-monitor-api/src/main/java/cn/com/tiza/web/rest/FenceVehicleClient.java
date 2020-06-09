package cn.com.tiza.web.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author tz0920
 */
@FeignClient(value = "feisi-monitor")
public interface FenceVehicleClient {

    /**
     * 删除该组织的所有围栏关联了此车的数据
     *
     * @param vin
     * @param orgId
     * @return
     */
    @DeleteMapping("/fence/deleteRelatedVehiclesByOrgId/{vin}/{orgId}")
    void deleteRelatedVehiclesByOrgId(@PathVariable("vin") String vin,
                                      @PathVariable("orgId") Long orgId);

    /**
     * 根据经纬度获取详细地址
     *
     * @param lon 经度
     * @param lat 纬度
     * @return 详细地址
     */
    @GetMapping("/fence/getLocation")
    String getLocation(@RequestParam("lon") String lon, @RequestParam("lat") String lat);

    /**
     * 删除车辆报警数据
     *
     * @param vin
     */
    @DeleteMapping("/alarmHistory/deleteByVin/{vin}")
    void deleteByVin(@PathVariable("vin") String vin);
}
