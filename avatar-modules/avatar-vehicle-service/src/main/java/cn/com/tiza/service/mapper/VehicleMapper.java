package cn.com.tiza.service.mapper;


import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.domain.Vehicle;
import cn.com.tiza.service.dto.VehicleDto;
import cn.com.tiza.web.rest.vm.VehicleServiceInfoVM;
import cn.com.tiza.web.rest.vm.VehicleVM;
import com.vip.vjtools.vjkit.time.DateFormatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Slf4j
@Component
public class VehicleMapper {

    public Vehicle dtoToEntity(VehicleDto dto) {
        if (dto == null) {
            return null;
        }
        Vehicle entity = new Vehicle();
        entity.setLoanPeriod(dto.getLoanPeriod());
        entity.setRegistDate(dto.getRegistDate());
        entity.setSaleDate(dto.getSaleDate());
        entity.setSaleMethod(dto.getSaleMethod());
        entity.setSaleStatus(dto.getSaleStatus());
        entity.setSeller(dto.getSeller());
        entity.setServiceEndDate(dto.getServiceEndDate());
        entity.setServicePeriod(dto.getServicePeriod());
        entity.setServiceStartDate(dto.getServiceStartDate());
        entity.setServiceStatus(dto.getServiceStatus());
        entity.setContractNumber(dto.getContractNumber());
        entity.setCreateTime(System.currentTimeMillis());
        entity.setCreateUserAccount(BaseContextHandler.getLoginName());
        entity.setCreateUserRealname(BaseContextHandler.getName());
        entity.setCustomerId(dto.getCustomerId());
        entity.setDescription(dto.getDescription());
        entity.setFinanceId(dto.getFinanceId());
        entity.setOrganizationId(dto.getOrganizationId());
        entity.setTerminalId(dto.getTerminalId());
        entity.setUpdateTime(System.currentTimeMillis());
        entity.setUpdateUserAccount(BaseContextHandler.getLoginName());
        entity.setUpdateUserRealname(BaseContextHandler.getName());
        entity.setVehicleModelId(dto.getVehicleModelId());
        entity.setVehicleTypeId(dto.getVehicleTypeId());
        entity.setVin(dto.getVin());
        return entity;
    }

    public VehicleVM toVM(Vehicle entity) {
        if (entity == null) {
            return null;
        }
        VehicleVM vm = new VehicleVM();
        vm.setId(entity.getId());
        vm.setLoanPeriod(entity.getLoanPeriod());
        vm.setSaleDate(entity.getSaleDate());
        vm.setSaleMethod(entity.getSaleMethod());
        vm.setSaleStatus(entity.getSaleStatus());
        vm.setSeller(entity.getSeller());
        vm.setServicePeriod(entity.getServicePeriod());
        vm.setContractNumber(entity.getContractNumber());
        vm.setVehicleModelId(entity.getVehicleModelId());
        vm.setVehicleTypeId(entity.getVehicleTypeId());
        vm.setVin(entity.getVin());
        vm.setOrganizationId(entity.getOrganizationId());
        vm.setCustomerId(entity.getCustomerId());
        vm.setFinanceId(entity.getFinanceId());
        return vm;
    }


    public VehicleServiceInfoVM toServiceInfoVM(Vehicle entity) {
        if (entity == null) {
            return null;
        }
        VehicleServiceInfoVM vm = new VehicleServiceInfoVM();
        vm.setId(entity.getId());
        vm.setServiceStatus(null == entity.getServiceStatus() ? null : entity.getServiceStatus().getValue());
        vm.setServicePeriod(entity.getServicePeriod());
        try {
            vm.setServiceStartDate(DateFormatUtil.parseDate("yyyyMMdd", String.valueOf(entity.getServiceStartDate())).getTime());
            vm.setServiceEndDate(DateFormatUtil.parseDate("yyyyMMdd", String.valueOf(entity.getServiceEndDate())).getTime());

        } catch (Exception e) {
            log.info(e.getMessage());
        }

        return vm;
    }

    public List<VehicleVM> entitiesToVMList(List<Vehicle> entities) {
        return entities.stream()
                .map(this::toVM)
                .collect(Collectors.toList());
    }
}
