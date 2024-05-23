package com.ward.ward_server.api.user.auth.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtDecoder jwtDecoder;
    private final JwtToPrincipalConverter jwtToPrincipalConverter;
    private final JwtProperties properties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        extractTokenFromRequest(request) // 헤더에서 JWT 토큰 추출
                .map(jwtDecoder::decode) // JWT 토큰 해독하여 사용자 정보 가져오기
                .filter(decodedJWT -> isAccessToken(decodedJWT)) // 추가된 부분
                .map(jwtToPrincipalConverter::convert) // 가져온 사용자 정보를 CustomUserDetails 객체로 변환, 생성
                .map(UserPrincipalAuthenticationToken::new) // UserPrincipalAuthenticationToken을 생성하여 인증된 사용자로 설정
                .ifPresent(authentication -> SecurityContextHolder.getContext().setAuthentication(authentication));
        // 앞에 UPAT 객체가 존재하면, 즉 토큰 검증,사용자 인증 성공했을 때 -> SecurityContextHolder에 현재 스레드에 대한 인증 객체를 설정
        // 이를 통해 사용자에 대한 정보 얻기

        filterChain.doFilter(request, response);
    }

    private Optional<String> extractTokenFromRequest(HttpServletRequest request) {
        var token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return Optional.of(token.substring(7));
        }
        return Optional.empty();
    }

    // access token 여부를 판별
    private boolean isAccessToken(DecodedJWT decodedJWT) {
        return decodedJWT.getExpiresAt().before(new Date(System.currentTimeMillis() + Duration.ofMinutes(properties.getAccessTokenValidity()).toMillis()));
    }
}
