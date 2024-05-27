package com.ward.ward_server.api.user.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ward.ward_server.global.exception.ExceptionCode;
import com.ward.ward_server.global.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

// Security에서 인증되지 않은 사용자의 접근 시 발생하는 예외를 처리하는 클래스
@Component
public class UnauthorizedHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        String exceptionMessage = authException.getMessage();
        ExceptionCode exceptionCode = determineExceptionCode(exceptionMessage);
        ApiResponse<?> apiResponse = ApiResponse.failure(exceptionCode);
        response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
    }

    private ExceptionCode determineExceptionCode(String message) {
        if (message.equals(ExceptionCode.NON_EXISTENT_EMAIL.getMessage())) {
            return ExceptionCode.NON_EXISTENT_EMAIL;
        } else if (message.equals(ExceptionCode.INVALID_USERNAME_OR_PASSWORD.getMessage())) {
            return ExceptionCode.INVALID_USERNAME_OR_PASSWORD;
        } else if (message.equals(ExceptionCode.INVALID_REFRESH_TOKEN.getMessage())) {
            return ExceptionCode.INVALID_REFRESH_TOKEN;
        }
        // 기본 예외 코드
        return ExceptionCode.INVALID_USERNAME_OR_PASSWORD;
    }
}
