package cn.com.tiza.web.util;

import cn.com.tiza.config.PatchcaConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@EnableConfigurationProperties(PatchcaConfiguration.class)
public class PatchcaHelper {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private PatchcaConfiguration properties;

    public boolean enable() {
        return properties.getEnable();
    }

    String key(String username, String timestamp) {
        return properties.getPrefix() + username + "-" + timestamp;
    }

    public void save(String username, String timestamp, String value) {
        redisTemplate.opsForValue().set(key(username, timestamp), value, properties.getExpireTime(), TimeUnit.MINUTES);
    }

    public String get(String username, String timestamp) {
        return redisTemplate.opsForValue().get(key(username, timestamp));
    }
}
