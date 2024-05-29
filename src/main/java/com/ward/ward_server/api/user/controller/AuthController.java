package com.ward.ward_server.api.user.controller;

import com.ward.ward_server.api.user.auth.security.JwtProperties;
import com.ward.ward_server.api.user.auth.security.JwtTokens;
import com.ward.ward_server.api.user.dto.AddSocialLoginRequest;
import com.ward.ward_server.api.user.dto.LoginRequest;
import com.ward.ward_server.api.user.dto.RegisterRequest;
import com.ward.ward_server.api.user.service.AuthService;
import com.ward.ward_server.global.exception.ApiException;
import com.ward.ward_server.global.exception.ExceptionCode;
import com.ward.ward_server.global.response.ApiResponse;
import com.ward.ward_server.global.response.ApiResponseMessage;
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
        if (!authService.isRegisteredUser(request.getEmail())) {
            // 이메일로 기존 회원 유무 판별
            throw new ApiException(ExceptionCode.NON_EXISTENT_USER);
        }
        if (!authService.isSameUser(request.getProvider(),request.getProviderId())) {
            // provider+providerId 로 기존 소셜 계정인지, 다른 계정인지 판별
            // 다른 소셜 로그인 진행 시 통합 진행. 프론트로 통합하시겠습니까? 기존 소셜 계정으로 로그인 하시겠습니까? 물어보고 돌아오기
            //TODO 기존 계정이 뭔지 표시하기
            throw new ApiException(ExceptionCode.EXISTENT_USER);
        }
        //로그인 진행
        JwtTokens tokens = authService.attemptLogin(request.getProvider(), request.getProviderId(), request.getEmail());
        return ApiResponse.ok(ApiResponseMessage.LOGIN_SUCCESS, tokens);
    }

    @PostMapping("/addSocialLogin")
    public ApiResponse<JwtTokens> addSocialLogin(@RequestBody @Validated AddSocialLoginRequest request) {
        JwtTokens tokens = authService.addSocialLogin(request.getProvider(), request.getProviderId(), request.getEmail());
        return ApiResponse.ok(ApiResponseMessage.ADD_SOCIALLOGIN_SUCCESS, tokens);
    }

    @PostMapping
    public ApiResponse<JwtTokens> register(@RequestBody @Validated RegisterRequest request) {
        JwtTokens tokens = authService.registerUser(request);
        return ApiResponse.ok(ApiResponseMessage.SIGNUP_SUCCESS, tokens);
    }

    @PostMapping("/refresh")
    public ApiResponse<JwtTokens> refresh(@RequestParam("refreshToken") String refreshToken) {
        JwtTokens tokens = authService.refresh(refreshToken);
        return ApiResponse.ok(ApiResponseMessage.TOKEN_REFRESH_SUCCESS, tokens);
    }

    @GetMapping("/checkNickname")
    public ApiResponse<Boolean> checkDuplicateNickname(@RequestParam("nickname") String nickname) {
        boolean checkDuplicateNickname = authService.checkDuplicateNickname(nickname);
        return ApiResponse.ok(ApiResponseMessage.NICKNAME_CHECK_SUCCESS, checkDuplicateNickname);
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestParam("refreshToken") String refreshToken) {
        authService.invalidateRefreshToken(refreshToken);
        return ApiResponse.ok(ApiResponseMessage.LOGOUT_SUCCESS);
    }

    //TODO 소셜 계정 이메일 정보 변경 시 정보 받을 경로

    //TODO 회원탈퇴, 연동 해제
}
