package com.ward.ward_server.api.user.auth.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.jwt")
public class JwtProperties {

    private String secretKey;
    private String password;
    private long accessTokenValidity; // 분 단위
    private long refreshTokenValidity; // 일 단위
}

