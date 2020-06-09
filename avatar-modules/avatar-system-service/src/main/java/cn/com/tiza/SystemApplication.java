package cn.com.tiza;

import cn.com.tiza.config.ApplicationProperties;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 系统管理服务
 * @author tiza
 */
@EnableFeignClients
@EnableSwagger2Doc
@SpringCloudApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class SystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class, args);
    }
}
