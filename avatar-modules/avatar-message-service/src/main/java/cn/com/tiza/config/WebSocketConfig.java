package cn.com.tiza.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 注入ServerEndpointExporter之后，
 *
 * 这个bean会自动注册使用了@ServerEndpoint注解声明的websocket
 *
 * @author tiza
 */
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }

}