package cn.com.tiza.service.mapper;

import cn.com.tiza.domain.Role;
import cn.com.tiza.service.dto.RoleCommand;
import cn.com.tiza.web.rest.vm.RoleVM;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * base_role实体和dto转换服务
 * @author jiajian
 */
@Component
public class RoleMapper {
    public Role commandToEntity(RoleCommand command){
        if(null == command){
            return null;
        }
        Role role = new Role();
        role.setRoleType(command.getRoleType());
        role.setFinanceId(command.getFinanceId());
        role.setOrganizationId(command.getOrganizationId());
        role.setRoleName(command.getRoleName());
        role.setRemark(command.getRemark());
        role.setCreatorInfo();
        return role;
    }

    public RoleVM roleToVM(Role entity) {
        if (null == entity) {
            return null;
        }
        RoleVM vm = new RoleVM();
        vm.setId(entity.getId());
        vm.setRoleType(entity.getRoleType());
        vm.setFinanceId(entity.getFinanceId());
        vm.setOrganizationId(entity.getOrganizationId());
        vm.setRoleName(entity.getRoleName());
        vm.setRemark(entity.getRemark());
        vm.setCreateUserAccount(entity.getCreateUserAccount());
        vm.setCreateTime(entity.getCreateTime());
        return vm;
    }

    public List<RoleVM> roleToVmList(List<Role> baseRoles) {
        return baseRoles.stream()
                .map(this::roleToVM)
                .collect(Collectors.toList());

    }

}
