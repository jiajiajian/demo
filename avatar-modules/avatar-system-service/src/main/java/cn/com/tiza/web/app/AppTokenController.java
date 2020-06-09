package cn.com.tiza.web.app;

import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.context.UserInfo;
import cn.com.tiza.security.auth.jwt.TokenProvider;
import cn.com.tiza.service.LogService;
import cn.com.tiza.service.UserService;
import cn.com.tiza.service.mapper.UserMapper;
import cn.com.tiza.web.rest.dto.LoginRequest;
import cn.com.tiza.web.rest.vm.AuthTokenVM;
import cn.com.tiza.web.rest.vm.UserVM;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

/**
 * @author tiza
 */
@Slf4j
@RestController
@RequestMapping("/app/auth")
public class AppTokenController {

    @Autowired
    private UserService userService;

    @Autowired
    private LogService logService;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * app 用户登陆
     * @param command
     * @return
     */
    @PostMapping("login")
    public ResponseEntity<AuthTokenVM> login(@RequestBody @Valid LoginRequest command) {
        return loginInternal(command);
    }

    private ResponseEntity<AuthTokenVM> loginInternal(LoginRequest command) {
        Optional<UserVM> optional = userService.login(command.getUsername(), command.getPassword());
        return optional.map(user -> userMapper.toAuthentication(user))
                .map(user -> {
                    logService.logLogin(user);
                    return ResponseEntity.ok(getToken(user, command.isRememberMe()));
                }).orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    private AuthTokenVM getToken(UserInfo authentication, boolean isRememberMe) {
        String token = tokenProvider.createAccessToken(authentication,isRememberMe);
        redisTemplate.opsForValue().set(token, authentication.toJson());
        return new AuthTokenVM(token, null);
    }

    @PostMapping("logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        logService.logLogOut(BaseContextHandler.getUser());
        return ResponseEntity.ok().build();
    }
}
