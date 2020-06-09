package cn.com.tiza.web.rest;

import cn.com.tiza.dto.SelectOption;
import cn.com.tiza.web.rest.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * vehicle feign client
 *
 * @author villas
 */
@FeignClient(value = "feisi-vehicle", fallback = FeignFailBack.class)
public interface VehicleClient {
    /**
     * 新增车辆
     *
     * @param dto dto
     */
    @PostMapping("/vehicle")
    void create(@RequestBody @Valid VehicleDto dto);

    /**
     * 获取车辆信息
     *
     * @param id id
     * @return 车辆信息
     */
    @GetMapping("/vehicle/{id}")
    VehicleVM get(@PathVariable("id") Long id);


    /**
     * 根据所选机构查询该机构根组织下所有车辆类型
     *
     * @param orgId 所选机构
     */
    @GetMapping("/vehicleType/optionsBySelectOrg/{orgId}")
    List<SelectOption> optionsBySelectOrg(@PathVariable("orgId") Long orgId);

    @GetMapping("/vehicleModel/vehicleModelOptionsByVehicleType/{vehicleTypeId}")
    List<SelectOption> vehicleModelOptionsByVehicleType(@PathVariable("vehicleTypeId") Long vehicleTypeId);

    @PostMapping("/pubFiles")
    String saveToDb(@RequestBody PubFileDto fileDto);

    /**
     * 获取redis车辆实时数据
     *
     * @param vin
     * @return 工况数据
     */
    @GetMapping("/vehicle/ciData/{vin}")
    Map<String, String> ciData(@PathVariable(value = "vin") String vin);

    /**
     * 获取解析之后的实时工况
     *
     * @param vin
     * @return
     */
    @GetMapping("/vehicleMonitor/getTrackData/{vin}")
    WorkConditionVM getTrackData(@PathVariable(value = "vin") String vin);

    /**
     * 获取解析之后的实时工况
     *
     * @param vin
     * @return
     */
 /*   @GetMapping("/vehicleMonitor/getFunctionItemListByVin/{vin}")
    List<FunctionSetItemVM> getFunctionItemListByVin(@PathVariable(value = "vin") String vin);*/

}
