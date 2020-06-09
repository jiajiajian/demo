package cn.com.tiza.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 系统配置
 *
 * @author tiza
 */
@Data
@Component
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

    private String terminalType;
    private String apiKey;
}
