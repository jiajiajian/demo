package cn.com.tiza.web.rest;

import cn.com.tiza.context.UserInfo;
import cn.com.tiza.security.auth.jwt.TokenProvider;
import cn.com.tiza.service.LogService;
import cn.com.tiza.service.UserService;
import cn.com.tiza.service.mapper.UserMapper;
import cn.com.tiza.web.rest.dto.LoginRequest;
import cn.com.tiza.web.rest.errors.BadRequestAlertException;
import cn.com.tiza.web.rest.vm.AuthTokenVM;
import cn.com.tiza.web.rest.vm.UserVM;
import cn.com.tiza.web.util.PatchcaHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

import static cn.com.tiza.web.rest.errors.SystemErrorConstants.USERNAME_PATCHCA_NOT_MATCH;

/**
 * @author tiza
 */
@Slf4j
@RestController
@RequestMapping("auth")
public class TokenController {

    @Autowired
    private UserService userService;

    @Autowired
    private LogService logService;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private PatchcaHelper patchcaHelper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;
    
    @PostMapping("jwt")
    public ResponseEntity login(@RequestBody @Valid LoginRequest command, HttpServletRequest request) {
        if (patchcaHelper.enable()) {
            String code = patchcaHelper.get(command.getUsername(), command.getTimestamp());
            if (!StringUtils.hasText(code) || !code.equalsIgnoreCase(command.getPatchca())) {
                throw new BadRequestAlertException("patchca.not_match", "", USERNAME_PATCHCA_NOT_MATCH);
            }
        }
        return loginInternal(command);
    }

    @PostMapping("sso")
    public ResponseEntity sso(@RequestBody @Valid LoginRequest command, HttpServletRequest request) {
        return loginInternal(command);
    }

    private ResponseEntity loginInternal(LoginRequest command) {
        Optional<UserVM> optional = userService.login(command.getUsername(), command.getPassword());
        return optional.map(user -> userMapper.toAuthentication(user))
                .map(user -> {
                    logService.logLogin(user);
                    return ResponseEntity.ok(getToken(user, command.isRememberMe()));
                }).orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    private AuthTokenVM getToken(UserInfo authentication, boolean isRememberMe) {
        String token = tokenProvider.createAccessToken(authentication,isRememberMe);
        redisTemplate.opsForHash().put("USER_TOKEN",authentication.getLoginName(),token);
        return new AuthTokenVM(token, null);
    }

    @GetMapping("parse")
    public UserInfo getAuthentication(@RequestParam String token) {
        if(tokenProvider.validateToken(token)) {
            return tokenProvider.getAuthentication(token);
        } else {
            return null;
        }
    }
}
