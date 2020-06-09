package cn.com.tiza.cmd.redis;

import cn.com.tiza.cmd.CommandMsg;
import cn.com.tiza.cmd.command.domain.Command;
import cn.com.tiza.cmd.command.service.CommandService;
import cn.com.tiza.config.ApplicationProperties;
import cn.com.tiza.dto.CommandMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

import static cn.com.tiza.cmd.redis.RedisService.CMD_KEY;
import static cn.com.tiza.cmd.redis.RedisService.CMD_KEY_BAK;

/**
 * 监听器
 *
 * @author villas
 */
@Slf4j
@Component
@ConditionalOnProperty("spring.redis.host")
public class KeyExpiredListener extends KeyExpirationEventMessageListener {

    @Autowired
    private RedisService redisService;

    @Autowired
    private CommandService commandService;

    @Autowired
    private ApplicationProperties properties;


    public KeyExpiredListener(@Autowired RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    /**
     * 这里是回调函数失效的时候回调用这个函数
     *
     * @param message message
     * @param pattern pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        //过期的key
        String key = new String(message.getBody(), StandardCharsets.UTF_8);
        if (key.startsWith(CMD_KEY)) {
            log.info("------------------> redis key 过期: key={}", key);
            String postfix = key.substring(key.indexOf(':') + 1);
            String json = redisService.get(CMD_KEY_BAK + postfix);
            log.info("==================> redis value is {}", json);
            CommandMsg cacheMsgBak = redisService.jsonToObj(json, CommandMsg.class);
            if (cacheMsgBak.getCount() <= properties.getCommand().getRetryNum()) {
                //重试发送指令
                log.info("------retry send cmd, count is {}", cacheMsgBak.getCount() + 1);
                commandService.sendRetryCmd(cacheMsgBak);
            } else {
                //超时，回写状态到业务库
                log.info("------time out");
                redisService.deleteRetryCache(cacheMsgBak);
                commandService.updateStatusTimeOut(cacheMsgBak);
                //超时通知
                Command command = commandService.getCommand(cacheMsgBak);
                CommandMessage cm = new CommandMessage(cacheMsgBak.getCmdId(), 2);
                commandService.inform(command.getUserId(), cm);
            }
        }
        super.onMessage(message, pattern);
    }
}
