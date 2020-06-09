package cn.com.tiza.websocket;

import cn.com.tiza.dto.CommandMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author villas
 */
@RestController
public class CommandController {

    @Autowired
    private AuthAlarmWebSocket webSocket;

    @PutMapping("/command/{userId}/msg")
    public ResponseEntity<Void> inform(@PathVariable Long userId, @RequestBody CommandMessage message){
        webSocket.inform(userId, message);
        return ResponseEntity.ok().build();
    }
}
