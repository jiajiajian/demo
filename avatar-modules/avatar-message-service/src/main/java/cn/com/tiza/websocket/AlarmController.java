package cn.com.tiza.websocket;

import cn.com.tiza.dto.AlarmMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author tiza
 */
@RestController
@RequestMapping("/alarms")
public class AlarmController {

    @Autowired
    private AuthAlarmWebSocket webSocket;

    @PutMapping()
    public void handle(@RequestBody AlarmMessage message) {
        webSocket.broadcast(message.getMessage(), message.getUserIds());
    }
}
