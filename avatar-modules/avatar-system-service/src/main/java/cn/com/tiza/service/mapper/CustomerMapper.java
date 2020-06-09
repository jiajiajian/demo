package cn.com.tiza.service.mapper;


import org.springframework.stereotype.Component;

import cn.com.tiza.domain.Customer;
import cn.com.tiza.service.dto.CustomerDto;
import cn.com.tiza.web.rest.vm.CustomerVO;

import java.util.List;
import java.util.stream.Collectors;

/**
*  Mapper
* gen by beetlsql 2020-03-06
* @author tiza
*/
@Component
public class CustomerMapper {

    public Customer dtoToEntity(CustomerDto dto) {
        if(dto == null) {
            return null;
        }
        Customer entity = new Customer();
        entity.setName(dto.getName());
        entity.setAlarmName(dto.getAlarmName());
        entity.setAlarmNumber(dto.getAlarmNumber());
        entity.setOrganizationId(dto.getOrganizationId());
        entity.setOwnerName(dto.getOwnerName());
        entity.setOwnerNumber(dto.getOwnerNumber());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setRemark(dto.getRemark());
        entity.setCreatorInfo();
        entity.setUpdateInfo();
        return entity;
    }

    public CustomerVO toVM(Customer entity) {
        if(entity == null) {
            return null;
        }
        CustomerVO vm = new CustomerVO();
        vm.setId(entity.getId());
        vm.setAlarmName(entity.getAlarmName());
        vm.setAlarmNumber(entity.getAlarmNumber());
        vm.setCreateTime(entity.getCreateTime());
        vm.setCreateUserAccount(entity.getCreateUserAccount());
        vm.setName(entity.getName());
        vm.setOrganizationId(entity.getOrganizationId());
        vm.setOwnerName(entity.getOwnerName());
        vm.setOwnerNumber(entity.getOwnerNumber());
        vm.setPhoneNumber(entity.getPhoneNumber());
        vm.setRemark(entity.getRemark());
        return vm;
    }

    public List<CustomerVO> entitiesToVMList(List<Customer> entities) {
        return entities.stream()
                .map(this::toVM)
                .collect(Collectors.toList());
    }
}
