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
//    private final UserService userService;
    private final JwtProperties properties;

    //TODO 현재 Email 만 같으면 로그인 처리 되게 되어있음.
    @PostMapping("/login")
    public ApiResponse<JwtTokens> login(@RequestBody @Validated LoginRequest request){
        JwtTokens tokens = authService.attemptLogin(request.getProvider(), request.getProviderId(), request.getEmail(), properties.getPassword());
        return ApiResponse.ok(tokens);
    }

    @PostMapping("/refresh")
    public ApiResponse<JwtTokens> refresh(@RequestParam("refreshToken") String refreshToken) {
        JwtTokens tokens = authService.refresh(refreshToken);
        return ApiResponse.ok(tokens);
    }

    //TODO 닉네임 중복 에러 반환
    @PostMapping
    public ApiResponse<JwtTokens> register(@RequestBody @Validated RegisterRequest request) {
        JwtTokens tokens = authService.registerUser(request);
        return ApiResponse.ok(tokens);
    }

    //TODO 관리자 권한 부여 방식 토의 필요
    //관리자 권한 부여
//    @PutMapping("/grantAdmin")
//    public ApiResponse<Void> grantAdminRole(@RequestBody @Validated RoleChangeRequest request) {
//        userService.grantAdminRole(request.userId());
//        return ApiResponse.ok();
//    }
//
//    // 사용자 권한 부여
//    @PutMapping("/grantUser")
//    public ApiResponse<Void> grantUserRole(@RequestBody @Validated RoleChangeRequest request) {
//        userService.grantUserRole(request.userId());
//        return ApiResponse.ok();
//    }

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
