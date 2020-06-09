package cn.com.tiza;

import cn.com.tiza.config.ApplicationProperties;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author TZ0781
 */
@EnableFeignClients
@EnableSwagger2Doc
@EnableConfigurationProperties(ApplicationProperties.class)
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ActivitiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActivitiApplication.class, args);
    }
}
