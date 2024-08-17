package com.ward.ward_server.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "apple")
public class AppleConfig {
    private String unlinkUrl;
    private String clientId;
    private String keyId;
    private String teamId;
    private String privateKey;
}
