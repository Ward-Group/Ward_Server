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
    //TODO 토큰 만료 기간 정하기
    private String secretKey;
    private String password;
    private long accessTokenValidity; // 예: 1일
    private long refreshTokenValidity; // 예: 30일
}

