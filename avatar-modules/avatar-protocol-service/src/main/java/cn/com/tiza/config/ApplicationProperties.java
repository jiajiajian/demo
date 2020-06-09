package cn.com.tiza.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author
 */
@Getter
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

    private final QuartzJob quartzJob = new QuartzJob();
    private final TStar tstar = new TStar();
    private final ForwardData forwardData = new ForwardData();

    @Getter
    @Setter
    public static class QuartzJob {
        /**
         * 默认设置为每日 23点 执行一次
         */
        private String forwardDataCompressCron;
    }

    @Getter
    @Setter
    public static class TStar {
        private String terminalType;
    }

    @Getter
    @Setter
    public static class ForwardData {
        /**
         * 转发数据生成目录
         */
        private String directory;
        /**
         * 转发数据压缩文件名称
         */
        private String compressFileName;
    }
}
