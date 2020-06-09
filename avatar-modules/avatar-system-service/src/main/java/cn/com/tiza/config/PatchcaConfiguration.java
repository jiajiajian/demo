package cn.com.tiza.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("auth.patchca")
public class PatchcaConfiguration {

    private String prefix= "tiza:avatar:patchca:";

    private Boolean enable = false;

    private Integer expireTime = 5;
}
