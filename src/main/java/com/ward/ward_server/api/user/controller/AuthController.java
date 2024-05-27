package com.ward.ward_server.api.user.controller;

import com.ward.ward_server.api.user.auth.security.JwtProperties;
import com.ward.ward_server.api.user.auth.security.JwtTokens;
import com.ward.ward_server.api.user.dto.LoginRequest;
import com.ward.ward_server.api.user.dto.RegisterRequest;
import com.ward.ward_server.api.user.service.AuthService;
import com.ward.ward_server.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtProperties properties;

    @PostMapping("/login")
    public ApiResponse<JwtTokens> login(@RequestBody @Validated LoginRequest request) {
        JwtTokens tokens = authService.attemptLogin(request.getProvider(), request.getProviderId(), request.getEmail(), properties.getPassword());
        return ApiResponse.ok(tokens);
    }

    @PostMapping("/refresh")
    public ApiResponse<JwtTokens> refresh(@RequestParam("refreshToken") String refreshToken) {
        JwtTokens tokens = authService.refresh(refreshToken);
        return ApiResponse.ok(tokens);
    }

    @PostMapping
    public ApiResponse<JwtTokens> register(@RequestBody @Validated RegisterRequest request) {
        JwtTokens tokens = authService.registerUser(request);
        return ApiResponse.ok(tokens);
    }

    @GetMapping("/checkNickname")
    public ApiResponse<Boolean> checkDuplicateNickname(@RequestParam("nickname") String nickname) {
        boolean checkDuplicateNickname = authService.checkDuplicateNickname(nickname);
        return ApiResponse.ok(checkDuplicateNickname);
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestParam("refreshToken") String refreshToken) {
        authService.invalidateRefreshToken(refreshToken);
        return ApiResponse.ok();
    }
}
