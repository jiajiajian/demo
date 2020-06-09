package cn.com.tiza;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * 手机APP 服务
 */
@SpringCloudApplication
public class AppServer {

    public static void main(String[] args) {
        SpringApplication.run(AppServer.class, args);
    }
}
