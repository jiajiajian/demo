package cn.com.tiza.web.rest;

import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.domain.User;
import cn.com.tiza.service.LogService;
import cn.com.tiza.service.SysFunctionService;
import cn.com.tiza.service.UserService;
import cn.com.tiza.service.mapper.UserMapper;
import cn.com.tiza.web.rest.vm.UserVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.Set;

/**
 * @author tiza
 */
@RestController
@RequestMapping("accounts")
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SysFunctionService sysFunctionService;

    @Autowired
    private LogService logService;

    @PostMapping("logout")
    public ResponseEntity logout(HttpServletRequest request) {
        logService.logLogOut(BaseContextHandler.getUser());
        return ResponseEntity.ok().build();
    }

    @GetMapping("current/{id}")
    public UserVM getUserInfo(@PathVariable Long id) {
        Optional<User> user = userService.get(id);
        if (user.isPresent()) {
            user.get().checkEnable();
            return userMapper.userToVM(user.get());
        }
        return null;
    }

    @GetMapping("current")
    public UserVM getCurrentInfo() {
        Long uid = BaseContextHandler.getUserID();
        return userService.get(uid).map(user -> {
            user.checkEnable();
            UserVM vm = userMapper.userToVM(user);
            vm.setAuthorizes(sysFunctionService.getFunctionIdsByUser(uid, BaseContextHandler.getLoginName()));
            return vm;
        }).orElse(null);
    }

    @GetMapping("/permission/check")
    public boolean checkPermission(@RequestParam("permission") int[] permissions) {
        Set<Long> pids = sysFunctionService.getFunctionIdsByUser(BaseContextHandler.getUserID(),
                BaseContextHandler.getLoginName());
        for (int i = 0; i < permissions.length; i++) {
            if (pids.contains((long) permissions[i])) {
                return true;
            }
        }
        return false;
    }
}
