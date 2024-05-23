package com.ward.ward_server.api.user.auth.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtTokens {
    private final String accessToken;
    private final String refreshToken;
}
