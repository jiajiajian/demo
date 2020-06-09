package cn.com.tiza;

import cn.com.tiza.config.ApplicationProperties;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author tiza
 */
@SpringCloudApplication
@EnableFeignClients
@EnableSwagger2Doc
@EnableConfigurationProperties(ApplicationProperties.class)
public class ProtocolApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProtocolApplication.class, args);
	}
}
