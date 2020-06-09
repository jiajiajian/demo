package cn.com.tiza;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author tiza
 */
@EnableFeignClients
@EnableSwagger2Doc
@SpringCloudApplication
public class GrampusApplication {

    public static void main(String[] args) {
        SpringApplication.run(GrampusApplication.class, args);
    }

}
