package com.ward.ward_server.api.user.auth.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ward.ward_server.global.exception.ExceptionCode;
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
        Optional<String> token = extractTokenFromRequest(request);

        if (token.isEmpty()) {
            // 비회원일 경우 SecurityContextHolder에 아무것도 설정하지 않고 필터를 통과하게 함
            filterChain.doFilter(request, response);
            return;
        }

        try {
            token.map(jwtDecoder::decode)
                    .filter(this::isAccessToken)
                    .map(jwtToPrincipalConverter::convert)
                    .map(UserPrincipalAuthenticationToken::new)
                    .ifPresent(authentication -> SecurityContextHolder.getContext().setAuthentication(authentication));
        } catch (TokenExpiredException ex) {
            request.setAttribute("JWT_EXCEPTION", ExceptionCode.TOKEN_EXPIRED);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
            return;
        } catch (JWTVerificationException ex) {
            request.setAttribute("JWT_EXCEPTION", ExceptionCode.INVALID_TOKEN);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return;
        } catch (IllegalArgumentException ex) {
            request.setAttribute("JWT_EXCEPTION", ExceptionCode.MISSING_AUTH_HEADER);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing Authorization header");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private Optional<String> extractTokenFromRequest(HttpServletRequest request) {
        var token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return Optional.of(token.substring(7));
        }
        return Optional.empty();
    }

    private boolean isAccessToken(DecodedJWT decodedJWT) {
        return decodedJWT.getExpiresAt().after(new Date());
    }
}
