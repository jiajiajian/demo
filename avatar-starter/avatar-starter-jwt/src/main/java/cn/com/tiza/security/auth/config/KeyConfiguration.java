package cn.com.tiza.security.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "auth.jwt", ignoreUnknownFields = false)
@Data
public class KeyConfiguration {

    String secret = null;
    String base64Secret = null;
    /**
     * 0.5 hour
    */
    long tokenValidityInSeconds = 1800;
    /**
     * 30 hours;
     */
    long tokenValidityInSecondsForRememberMe = 2592000;

}
