package com.ward.ward_server.api.user.controller;

import com.ward.ward_server.api.user.auth.security.JwtTokens;
import com.ward.ward_server.api.user.dto.AddSocialLoginRequest;
import com.ward.ward_server.api.user.service.AuthService;
import com.ward.ward_server.global.response.ApiResponse;
import com.ward.ward_server.global.response.ApiResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/auth")
public class UserAuthController {
    private final AuthService authService;

    @PostMapping("/addSocialLogin")
    public ApiResponse<JwtTokens> addSocialLogin(@RequestBody @Validated AddSocialLoginRequest request) {
        JwtTokens tokens = authService.addSocialLogin(request.provider(), request.providerId(), request.email());
        return ApiResponse.ok(ApiResponseMessage.ADD_SOCIALLOGIN_SUCCESS, tokens);
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestParam("refreshToken") String refreshToken) {
        authService.invalidateRefreshToken(refreshToken);
        return ApiResponse.ok(ApiResponseMessage.LOGOUT_SUCCESS);
    }

    //TODO 회원탈퇴
//    @PostMapping("/deleteAccount")
//    public ApiResponse<Void> deleteAccount(@RequestParam("userId") String userId) {
//        authService.deleteAccount(userId);
//        return ApiResponse.ok(ApiResponseMessage.ACCOUNT_DELETION_SUCCESS);
//    }
//
//    //TODO 연동 해제
//    @PostMapping("/disconnectSocialLogin")
//    public ApiResponse<Void> disconnectSocialLogin(@RequestParam("userId") String userId, @RequestParam("provider") String provider, @RequestParam("providerId") String providerId) {
//        authService.disconnectSocialLogin(userId, provider, providerId);
//        return ApiResponse.ok(ApiResponseMessage.SOCIAL_LOGIN_DISCONNECT_SUCCESS);
//    }

    //TODO 소셜 계정 이메일 정보 변경 시 정보 받을 경로

}
