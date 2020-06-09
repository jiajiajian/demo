package cn.com.tiza.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 系统配置
 * @author tiza
 */
@Data
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {


}
