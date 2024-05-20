package com.ward.ward_server.api.user.auth.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtIssuer {
    private final JwtProperties properties;

    //TODO access token 기간 정하기
    public String issueAccessToken(long userId, String email, List<String> roles) {
        Date issuedAt = new Date();
        Date expiresAt = new Date(System.currentTimeMillis() + Duration.ofDays(1).toMillis());

        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .withClaim("e", email)
                .withClaim("r", roles)
                .sign(Algorithm.HMAC256(properties.getSecretKey()));
    }

    public String issueRefreshToken(long userId, String email, List<String> roles) {
        Date issuedAt = new Date();
        Date expiresAt = new Date(System.currentTimeMillis() + Duration.ofDays(30).toMillis());

        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .withClaim("e", email)
                .withClaim("r", roles)
                .sign(Algorithm.HMAC256(properties.getSecretKey()));
    }
}
