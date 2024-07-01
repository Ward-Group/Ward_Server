package com.ward.ward_server.api.user.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ward.ward_server.global.exception.ExceptionCode;
import com.ward.ward_server.global.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        ApiResponse<?> apiResponse = ApiResponse.failure(ExceptionCode.ACCESS_DENIED, "접근 권한이 부족합니다.");
        response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
    }
}
