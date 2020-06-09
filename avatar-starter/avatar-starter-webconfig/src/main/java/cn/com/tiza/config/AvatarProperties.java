package cn.com.tiza.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author tiza
 */
@Component
@ConfigurationProperties(prefix = "avatar")
public class AvatarProperties {

    private final Async async = new Async();

    public Async getAsync() {
        return async;
    }

    public static class Async {

        private int corePoolSize = AvatarDefaults.Async.corePoolSize;

        private int maxPoolSize = AvatarDefaults.Async.maxPoolSize;

        private int queueCapacity = AvatarDefaults.Async.queueCapacity;

        public int getCorePoolSize() {
            return corePoolSize;
        }

        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaxPoolSize() {
            return maxPoolSize;
        }

        public void setMaxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public int getQueueCapacity() {
            return queueCapacity;
        }

        public void setQueueCapacity(int queueCapacity) {
            this.queueCapacity = queueCapacity;
        }
    }

}
