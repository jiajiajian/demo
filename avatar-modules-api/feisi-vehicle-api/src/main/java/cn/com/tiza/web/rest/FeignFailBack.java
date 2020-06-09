package cn.com.tiza.web.rest;

import cn.com.tiza.dto.SelectOption;
import cn.com.tiza.web.rest.dto.*;
import cn.com.tiza.web.rest.errors.BadRequestAlertException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author villas
 */
@Component
@Slf4j
public class FeignFailBack implements VehicleClient {

    @Override
    public void create(@Valid VehicleDto dto) {
        log.error("create vehicle through terminal service is failed, dto: {}", dto);
        throw new BadRequestAlertException("feign invoke is error", dto.toString(), "feign.create.dto.error");
    }

    @Override
    public VehicleVM get(Long id) {
        return null;
    }

    @Override
    public List<SelectOption> optionsBySelectOrg(Long orgId) {
        log.error("get vehicle type through terminal service is failed, orgId: {}", orgId);
        throw new BadRequestAlertException("feign invoke is error", orgId.toString(), "feign.get.vehicle.type.error");
    }

    @Override
    public List<SelectOption> vehicleModelOptionsByVehicleType(Long vehicleTypeId) {
        log.error("get vehicle model through terminal service is failed, vehicleTypeId: {}", vehicleTypeId);
        throw new BadRequestAlertException("feign invoke is error", vehicleTypeId.toString(), "feign.get.vehicle.model.error");
    }
    @Override
    public String saveToDb( PubFileDto fileDto) {
        log.error("save file terminal service is failed, fileId: {}", fileDto.getName());
        throw new BadRequestAlertException("feign invoke is error", fileDto.getName().toString(), "feign.save.file.error");
    }

    @Override
    public Map<String, String> ciData(String vin) {
        log.error("vehicle ciData error {}",vin);
        throw new BadRequestAlertException("feign invoke is error", vin, "feign.get.vehicle.cidata.error");
    }

    @Override
    public WorkConditionVM getTrackData(String vin) {
        log.error("vehicle getTrackData error {}",vin);
        throw new BadRequestAlertException("feign invoke is error", vin, "feign.get.vehicle.getTrackData.error");
    }

   /* @Override
    public List<FunctionSetItemVM> getFunctionItemListByVin(String vin) {
        log.error("vehicle getFunctionItemListByVin error {}",vin);

        throw new BadRequestAlertException("feign invoke is error", vin, "feign.get.vehicle.getFunctionItemListByVin.error");
    }*/
}
