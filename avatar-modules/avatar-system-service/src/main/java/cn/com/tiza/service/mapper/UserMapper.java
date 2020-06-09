package cn.com.tiza.service.mapper;

import cn.com.tiza.context.UserInfo;
import cn.com.tiza.domain.AppConfig;
import cn.com.tiza.domain.User;
import cn.com.tiza.service.dto.UserCommand;
import cn.com.tiza.web.app.vm.AppUserVO;
import cn.com.tiza.web.rest.vm.UserVM;
import com.vip.vjtools.vjkit.mapper.JsonMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * User实体和DTO转换服务,不使用BeanUtils.copyProperties
 *
 * @author tiza
 */
@Component
public class UserMapper {

    public User commandToUser(UserCommand command) {
        if (command == null) {
            return null;
        }
        User user = new User();
        user.setLoginName(command.getLoginName());
        user.setFinanceId(command.getFinanceId());
        user.setUserType(command.getUserType());
        user.setOrganizationId(command.getOrganizationId());
        user.setRealname(command.getRealName());
        user.setEmailAddress(command.getEmailAddress());
        user.setPhoneNumber(command.getPhoneNumber());
        user.setTelephoneNumber(command.getTelephoneNumber());
        user.setContactAddress(command.getContactAddress());
        user.setRemark(command.getRemark());
        user.setExpirationDate(command.getExpirationDate());
        return user;
    }

    public UserVM userToVM(User user) {
        if (user == null) {
            return null;
        }
        UserVM vm = new UserVM();
        vm.setId(user.getId());
        vm.setLoginName(user.getLoginName());
        vm.setUserType(user.getUserType());
        vm.setRootOrgId(user.getRootOrgId());
        vm.setOrganizationId(user.getOrganizationId());
        vm.setFinanceId(user.getFinanceId());
        vm.setRealName(user.getRealname());
        vm.setEmailAddress(user.getEmailAddress());
        vm.setPhoneNumber(user.getPhoneNumber());
        vm.setExpirationDate(user.getExpirationDate());
        vm.setCreateTime(user.getCreateTime());
        vm.setRoleName(user.getRoleName());
        vm.setRoleIds(user.getRoleIds());
        return vm;
    }

    public List<UserVM> userListToVMList(List<User> users) {
        return users.stream()
                .map(this::userToVM)
                .collect(Collectors.toList());
    }

    public AppUserVO userToAppVO(User entity) {
        if (entity == null) {
            return null;
        }
        AppUserVO vm = new AppUserVO();
        vm.setId(entity.getId());
        vm.setUserType(entity.getUserType());
        vm.setContactAddress(entity.getContactAddress());
        vm.setEmailAddress(entity.getEmailAddress());
        vm.setFinanceId(entity.getFinanceId());
        vm.setLoginName(entity.getLoginName());
        vm.setOrganizationId(entity.getOrganizationId());
        vm.setPhoneNumber(entity.getPhoneNumber());
        vm.setRealName(entity.getRealname());
        vm.setOrgName(entity.getOrgName());
        vm.setFinanceName(entity.getFinanceName());
        vm.setTelephoneNumber(entity.getTelephoneNumber());
        if (entity.getAppConfig() != null && entity.getAppConfig().trim().length() > 0) {
            vm.setAppConfig(JsonMapper.defaultMapper().fromJson(entity.getAppConfig(), AppConfig.class));
        }
        return vm;
    }

    public UserInfo toAuthentication(UserVM user){
        return UserInfo.builder()
                .id(user.getId())
                .userType(user.getUserType())
                .realName(user.getRealName())
                .orgId(user.getOrganizationId())
                .rootOrgId(user.getRootOrgId())
                .financeId(user.getFinanceId())
                .loginName(user.getLoginName())
                .build();
    }
}
