package com.ward.ward_server.api.user.controller;

import com.ward.ward_server.api.user.dto.LoginRequest;
import com.ward.ward_server.api.user.dto.RegisterRequest;
import com.ward.ward_server.api.user.service.AuthService;
import com.ward.ward_server.api.user.auth.security.JwtProperties;
import com.ward.ward_server.global.response.ApiResponse;
import com.ward.ward_server.global.response.error.ErrorCode;
import com.ward.ward_server.global.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtProperties properties;

    @PostMapping("/login")
    public ApiResponse login(@RequestBody @Validated LoginRequest request){
        String token = authService.attemptLogin(request.getProvider(), request.getProviderId(), request.getEmail(), properties.getPassword());

        if (token.startsWith("[ERROR]")) {
            return ApiResponse.failure(ErrorCode.NON_EXISTENT_EMAIL);
        }
        return ApiResponse.ok(token);
    }

    @PostMapping("/registerUser")
    public ApiResponse register(@RequestBody @Validated RegisterRequest request) {
        //회원 가입 하고 성공 여부만 or jwt 반환하여 로그인까지?
        authService.registerUser(request);

        //일단 메세지만 반환 나중에 jwt 반환으로 변경?
        return ApiResponse.ok(Constants.SUCCESSFUL_REGISTRATION);
    }
}
