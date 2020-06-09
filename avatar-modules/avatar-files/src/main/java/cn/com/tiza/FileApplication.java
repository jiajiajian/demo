package cn.com.tiza;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 文件服务，包括存db、存磁盘、存FTP
 *
 * @author tiza
 */
@SpringCloudApplication
@EnableFeignClients
@EnableSwagger2Doc
public class FileApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileApplication.class, args);
    }
}
