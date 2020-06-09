package cn.com.tiza.service;

import cn.com.tiza.config.ApplicationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class LockService {

    private ApplicationProperties.Auth auth;

    private StringRedisTemplate redisTemplate;

    private String attemptKey = "feisi:auth:userattempt";
    private String lockKeyPrefix = "feisi:auth:locked:";

    @Autowired
    public LockService(ApplicationProperties properties, StringRedisTemplate redisTemplate) {
        this.auth = properties.getAuth();
        this.redisTemplate = redisTemplate;
    }

    /**
     * 查询用户连续登陆失败次数
     *
     * @param username
     */
    public boolean isUserLocked(String username) {
        if (!auth.isLockEnable()) {
            return false;
        }
        String key = lockKeyPrefix + username;
        return redisTemplate.opsForValue().get(key) != null;
    }

    /**
     * 连续登陆失败达到5次，就锁定账户登陆30分钟，并返回true（已锁定）
     *
     * @param username username
     * @return 是否被锁定
     */
    public boolean increaseUserAttempt(String username) {

        if (!auth.isLockEnable()) {
            return false;
        }
        Integer attemptTimes = getUserAttempt(username);
        attemptTimes += 1;
        //超过连续登陆错误次数-写入锁定数据
        if (attemptTimes >= auth.getFailTimes()) {
            String key = lockKeyPrefix + username;
            redisTemplate.opsForValue().set(key, attemptTimes + "", auth.getLockMinutes(), TimeUnit.MINUTES);
            clear(username);
            return true;
        } else {
            redisTemplate.opsForHash().increment(attemptKey, username, 1);
            return false;
        }
    }

    public Integer getUserAttempt(String username) {
        Object value = redisTemplate.opsForHash().get(attemptKey, username);
        if (value == null) {
            return 0;
        }
        return Integer.parseInt(value.toString());
    }

    /**
     * 当返回auth.getFailTimes()的值时说明已经被锁定
     *
     * @param username username
     * @return 剩余的重试次数
     */
    public int getRetryNum(String username) {
        return auth.getFailTimes() - getUserAttempt(username);
    }

    public Long getExpire(String username) {
        String key = lockKeyPrefix + username;
        return redisTemplate.getExpire(key, TimeUnit.MINUTES);
    }

    /**
     * 登陆成功/锁定账户时-清除密码失败次数
     *
     * @param username
     */
    public void clear(String username) {
        if (!auth.isLockEnable()) {
            return;
        }
        redisTemplate.opsForHash().delete(attemptKey, username);
    }

}
