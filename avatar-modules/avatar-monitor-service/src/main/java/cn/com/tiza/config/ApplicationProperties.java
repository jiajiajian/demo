package cn.com.tiza.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 应用配置信息
 *
 * @author tiza
 */
@Getter
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

    private final Kafka kafka = new Kafka();
    private final AMap aMap = new AMap();

    private final JPush jpush = new JPush();

    private final Sms sms = new Sms();

    private final Notice notice = new Notice();

    private final Report report = new Report();

    private final HttpPool pool = new HttpPool();

    private final QuartzJob quartzJob = new QuartzJob();

    @Getter
    @Setter
    public static class Report {

        private Long offlineTime = 300_000L;
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

    @Getter
    @Setter
    public static class QuartzJob {
        private String vehicleServiceExpireCron;

        private String controllerLockCron;

        private String maintenanceLogCron;

        private String forwardDataCompressCron;
    }

    @Getter
    @Setter
    public static class AMap {
        private String geocoder;
        private String regeocoder;
        private String key;
    }

    @Getter
    @Setter
    public static class JPush {
        /**
         * 应用key
         */
        private String appKey;
        /**
         * 应用秘钥
         */
        private String masterSecret;

    }

    @Getter
    @Setter
    public static class Notice {
        private String from;
    }

    @Data
    public static class Sms {
        int maxTimes = 5;

        int minus = 10;

        private String url;
        private String appKey;
        /**
         * APP_Secret
         */
        private String appSecret;
        /**
         * 国内短信签名通道号或国际/港澳台短信通道号
         */
        private String sender;
        /**
         * 报警模板ID
         */
        private String alarmTemplateId;
        private String faultTemplateId;
        private String fenceTemplateId;

        //条件必填,国内短信关注,当templateId指定的模板类型为通用模板时生效且必填,必须是已审核通过的,与模板类型一致的签名名称
        //国际/港澳台短信不用关注该参数
        /**
         * 签名名称
         */
        private String signature;
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
