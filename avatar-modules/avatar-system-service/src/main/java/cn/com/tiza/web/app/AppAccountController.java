package cn.com.tiza.web.app;

import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.domain.AppConfig;
import cn.com.tiza.service.UserService;
import cn.com.tiza.service.mapper.UserMapper;
import cn.com.tiza.web.app.vm.AppUserVO;
import cn.com.tiza.web.app.vm.PasswordChangeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * app我的账户
 */
@Slf4j
@RestController
@RequestMapping("/app/account")
public class AppAccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("current")
    public AppUserVO getCurrentInfo() {
        Long uid = BaseContextHandler.getUserID();
        return userService.getAppUserInfo(uid).map(user -> {
            user.checkEnable();
            AppUserVO vm = userMapper.userToAppVO(user);
            return vm;
        }).orElse(null);
    }

    /**
     * 修改密码
     * @param command
     * @return
     */
    @PutMapping("modifyPwd")
    public Integer modifyPwd(@RequestBody PasswordChangeDTO command) {
        return userService.changePassword(command.getOldPassword(), command.getNewPassword(), command.getNewPasswordConfirm());
    }

    /**
     * 设置用户参数
     * @param command
     * @return
     */
    @PutMapping("config")
    public void config(@RequestBody AppConfig command) {
        userService.updateAppConfig(BaseContextHandler.getUserID(), command);
    }

}
