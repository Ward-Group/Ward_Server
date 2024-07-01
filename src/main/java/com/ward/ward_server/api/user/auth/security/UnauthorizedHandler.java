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

@Component
public class UnauthorizedHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ExceptionCode exceptionCode = (ExceptionCode) request.getAttribute("JWT_EXCEPTION");
        if (exceptionCode == null) {
            exceptionCode = determineExceptionCode(authException.getMessage());
        }

        ApiResponse<?> apiResponse = ApiResponse.failure(exceptionCode);
        response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
    }

    private ExceptionCode determineExceptionCode(String message) {
        if (message.contains("Token expired")) {
            return ExceptionCode.TOKEN_EXPIRED;
        } else if (message.contains("Invalid token")) {
            return ExceptionCode.INVALID_TOKEN;
        } else if (message.contains("Missing Authorization header")) {
            return ExceptionCode.MISSING_AUTH_HEADER;
        }
        return ExceptionCode.INVALID_USERNAME_OR_PASSWORD;
    }
}
