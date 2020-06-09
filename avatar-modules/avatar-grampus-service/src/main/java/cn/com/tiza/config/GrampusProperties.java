package cn.com.tiza.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 * @author tiza
 */
@Data
@ConfigurationProperties(prefix = "application")
public class GrampusProperties {

    private final HttpPool pool = new HttpPool();

    private final Grampus grampus = new Grampus();

    @Getter
    @Setter
    public class Grampus {

        private String dataInterface;

        private String cmdInterface;

        private String tablePrefix;

        private String cmdQueryId;

        private String tempPath;

        private String trackData;

        private String rawData;
    }

    @Getter
    @Setter
    public static class HttpPool {
        private Integer maxTotal = 50;
        private Integer defaultMaxPerRoute;
        private Integer connectTimeout;
        private Integer connectionRequestTimeout;
        private Integer socketTimeout = 500;
        private Integer validateAfterInactivity = 300;
        private Boolean staleConnectionCheckEnabled = true;
    }
}
