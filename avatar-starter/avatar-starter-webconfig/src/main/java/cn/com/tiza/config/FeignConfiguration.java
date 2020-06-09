package cn.com.tiza.config;

import cn.com.tiza.constant.Constants;
import cn.com.tiza.context.BaseContextHandler;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author tiza
 */
@Slf4j
@Configuration
public class FeignConfiguration {

    @Bean
    public RequestInterceptor headerFeignInterceptor() {
        return template -> {
            String userJson = BaseContextHandler.getToken();
            log.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~FeignConfiguration user json is {}", userJson);
            if (userJson != null) {
                template.header(Constants.USER_INFO_HEADER, userJson);
            }
        };
    }

}
