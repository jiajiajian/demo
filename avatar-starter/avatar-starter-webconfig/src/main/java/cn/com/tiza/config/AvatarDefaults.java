package cn.com.tiza.config;

public interface AvatarDefaults {

    interface Async {

        int corePoolSize = 2;
        int maxPoolSize = 50;
        int queueCapacity = 10000;
    }

}
