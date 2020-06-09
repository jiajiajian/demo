package cn.com.tiza.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 系统配置
 * @author tiza
 */
@Getter
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

    public final CommandConfig command = new CommandConfig();

    private final Kafka kafka = new Kafka();

    @Getter
    @Setter
    public static class CommandConfig {
        private boolean enableRetry = true;
        private int expire = 8;
        private int retryNum = 3;
    }

    @Getter
    @Setter
    public static class Kafka {

        private String bootstrapServers;

        private String groupId = "alarm-group";

        private String offsetReset = "earliest";

        private String protocol;

        private String mechanism;

        private String serviceName;

        private boolean enable = false;

        private String keyTab;

        private String principal;

    }
}
