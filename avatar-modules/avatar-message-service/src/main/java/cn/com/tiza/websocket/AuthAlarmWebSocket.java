package cn.com.tiza.websocket;

import cn.com.tiza.SpringUtil;
import cn.com.tiza.context.UserInfo;
import cn.com.tiza.dto.Message;
import cn.com.tiza.security.auth.jwt.TokenProvider;
import com.vip.vjtools.vjkit.mapper.JsonMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 校验用户认证WebSocket
 *
 * @author tiza
 */
@Slf4j
@ServerEndpoint("/websocket/alarms/auth/{ticket}")
@Component
public class AuthAlarmWebSocket {

    /**
     * 用来记录userId和该session之间的绑定关系.
     */
    private static Map<String, Session> sessionMap = new HashMap<>();
    private static Map<Long, String> userSessionMap = new HashMap<>();
    private static Map<String, Long> sessionUserMap = new HashMap<>();

    /**
     * 成功建立连接调用的方法.
     */
    @SneakyThrows
    @OnOpen
    public void onOpen(Session session, @PathParam("ticket") String ticket) {
        TokenProvider tokenProvider = SpringUtil.getBean(TokenProvider.class);
        if (tokenProvider.validateToken(ticket)) {
            UserInfo jwtInfo = tokenProvider.getAuthentication(ticket);
            Long uid = jwtInfo.getId();
            if (userSessionMap.containsKey(uid)) {
                clear(uid);
            }
            log.info("webSocket connected ! {}", jwtInfo.getLoginName());
            sessionMap.put(session.getId(), session);
            userSessionMap.put(uid, session.getId());
            sessionUserMap.put(session.getId(), uid);
        }
    }

    /**
     * 收到客户端消息后调用的方法.
     */
    @OnMessage
    public void onMessage(String text, Session session) {
        if (!"heartBeats".equalsIgnoreCase(text)) {
            log.info("accept : {}", text);
        }
        //发送消息，维持会话
        this.sendText(session, "echo : " + text);
    }

    /**
     * 连接关闭调用的方法.
     */
    @OnClose
    public void onClose(Session session) {
        String sid = session.getId();
        sessionMap.remove(sid);
        log.info("userId {} 断开链接", sessionUserMap.get(sid));
        Optional.ofNullable(sessionUserMap.remove(sid))
                .ifPresent(uid -> userSessionMap.remove(uid));
    }

    /**
     * 发生错误时调用.
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.info("发生错误");
        error.printStackTrace();
    }

    public void inform(Long userId, Object obj) {
        String sessionId = userSessionMap.get(userId);
        Session session = sessionMap.get(sessionId);
        if (session != null && session.isOpen()) {
            String msg = JsonMapper.defaultMapper().toJson(obj);
            log.info("inform success, userId = {}, msg is {}", userId, msg);
            this.sendText(session, msg);
        }
    }

    private synchronized void sendText(Session session, String msg){
        try {
            session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("sendText error : {}", e.getLocalizedMessage());
        }
    }

    /**
     * 群发的方法.
     *
     * @param message
     * @uids 要通知的用户
     */
    public void broadcast(Message message, List<Long> uids) {
        uids.forEach(uid -> inform(uid, message));
    }

    private void clear(Long uid) {
        Optional.ofNullable(userSessionMap.get(uid))
                .ifPresent(sid -> {
                    sessionMap.remove(sid);
                    sessionUserMap.remove(sid);
                    userSessionMap.remove(uid);
                });
    }
}
