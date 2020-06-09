package cn.com.tiza.web.rest;

import cn.com.tiza.dto.AlarmMessage;
import cn.com.tiza.dto.CommandMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author tz0920
 */
@FeignClient("feisi-message")
public interface WebSocketClient {

    @PutMapping("/alarms")
    void handle(@RequestBody AlarmMessage message);

    /**
     * 实时指令的结果，通知到页面
     * @param message 消息
     * @param userId userId
     */
    @PutMapping("/command/{userId}/msg")
    void inform(@PathVariable("userId") Long userId, @RequestBody CommandMessage message);
}
