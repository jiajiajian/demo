package cn.com.tiza;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author tiza
 */
@SpringCloudApplication
@EnableFeignClients
@EnableSwagger2Doc
public class VehicleApplication {
    public static void main(String[] args) {
        SpringApplication.run(VehicleApplication.class, args);
    }
}
