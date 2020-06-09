package cn.com.tiza.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfiguration {

    @Bean(name = "objectMapper")
    public ObjectMapper customObjectMapper() {
        return new CustomObjectMapper();
    }

}
