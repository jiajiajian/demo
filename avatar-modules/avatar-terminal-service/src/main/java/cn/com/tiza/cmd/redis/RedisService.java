package cn.com.tiza.cmd.redis;

import cn.com.tiza.cmd.CommandMsg;
import cn.com.tiza.cmd.command.service.CommandService;
import cn.com.tiza.config.ApplicationProperties;
import cn.com.tiza.web.rest.errors.InternalServerErrorException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * redis service
 *
 * @author villas
 */
@Slf4j
@Component
public class RedisService {

    public static final String CMD_KEY = "cmd_send_original:";

    public static final String CMD_KEY_BAK = "cmd_send_bak:";

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ApplicationProperties properties;


    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }


    public void setWithExpireSecond(String key, String json, Integer second) {
        redisTemplate.opsForValue().set(key, json, second, TimeUnit.SECONDS);
    }

    /**
     * 将实时指令缓存起来，并设置过期时间
     *
     * @param msg    msg
     * @param second 秒数
     */
    public void cacheCommand(CommandMsg msg, int second) {
        log.info("cmd is cache, msg is {}", msg);
        this.setWithExpireSecond(assembleKey(msg, CMD_KEY), objToJson(msg), second);
        this.setWithExpireSecond(assembleKey(msg, CMD_KEY_BAK), objToJson(msg), second + 4);
    }

    public CommandMsg getMsg(CommandMsg msg) {
        return jsonToObj(get(assembleKey(msg, CMD_KEY_BAK)), CommandMsg.class);
    }

    public String assembleKey(CommandMsg msg, String key) {
        return key + msg.getVin() + msg.getCmdId() + msg.getSerialNo() + msg.getDate();
    }

    public boolean isCache(CommandMsg msg) {
        return Objects.equals(redisTemplate.hasKey(assembleKey(msg, CMD_KEY_BAK)), Boolean.TRUE);
    }

    public void deleteRetryCache(CommandMsg msg) {
        redisTemplate.delete(ImmutableList.of(assembleKey(msg, CMD_KEY), assembleKey(msg, CMD_KEY_BAK)));
    }

    public <T> T jsonToObj(String json, Class<T> t) {
        try {
            return mapper.readValue(json, t);
        } catch (JsonProcessingException e) {
            log.error("RedisService jsonToObj exception: {}, param json is {}", e.getLocalizedMessage(), json);
            throw new InternalServerErrorException(e.getLocalizedMessage());
        }
    }

    public String objToJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("RedisService objToJson exception: {}", e.getLocalizedMessage());
            throw new InternalServerErrorException(e.getOriginalMessage());
        }
    }

    public void retryProcess(CommandMsg msg) {
        if (isCache(msg)) {
            log.info("cmd is not be cache");
            CommandMsg cacheMsgBak = getMsg(msg);
            msg.setCount(cacheMsgBak.getCount() + 1);
        }
        try {
            log.info("--------------cacheCommand--------------");
            CommandService.executor.submit(() -> cacheCommand(msg, properties.getCommand().getExpire()));
        } catch (Exception e) {
            log.error("KafkaCommandListener retryProcess exception: {}", e.getLocalizedMessage());
        }

    }
}
