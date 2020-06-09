package cn.com.tiza.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 系统配置
 * @author tiza
 */
@Data
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

    private final Auth auth = new Auth();

    @Data
    public static class Auth {
        private boolean lockEnable = true;

        private int failTimes = 5;

        private int lockMinutes = 20;
    }

}
