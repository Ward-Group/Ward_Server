package com.ward.ward_server.api.user.controller;

import com.ward.ward_server.api.user.auth.security.JwtProperties;
import com.ward.ward_server.api.user.auth.security.JwtTokens;
import com.ward.ward_server.api.user.dto.LoginRequest;
import com.ward.ward_server.api.user.dto.RegisterRequest;
import com.ward.ward_server.api.user.service.AuthService;
import com.ward.ward_server.api.user.service.UserService;
import com.ward.ward_server.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final JwtProperties properties;

    //TODO 현재 Email 만 같으면 로그인 처리 되게 되어있음.
    // 닉네임 중복 체크
    @PostMapping("/login")
    public ApiResponse login(@RequestBody @Validated LoginRequest request){
        JwtTokens tokens = authService.attemptLogin(request.getProvider(), request.getProviderId(), request.getEmail(), properties.getPassword());

        return ApiResponse.ok(tokens);
    }

    @PostMapping("/refresh")
    public ApiResponse refresh(@RequestParam("refreshToken") String refreshToken) {
        JwtTokens tokens = authService.refresh(refreshToken);

        return ApiResponse.ok(tokens);
    }

    @PostMapping("/registerUser")
    public ApiResponse register(@RequestBody @Validated RegisterRequest request) {
        // 회원가입 메서드에서 JWT 토큰을 반환하도록 수정됨
        JwtTokens tokens = authService.registerUser(request);
        return ApiResponse.ok(tokens);
    }

    //관리자 권한 부여
    @PutMapping("/grantAdmin/{userId}")
    public ApiResponse grantAdminRole(@PathVariable("userId") Long userId) {
        userService.grantAdminRole(userId);
        return ApiResponse.ok();

    }

    //사용자 권한 부여
    @PutMapping("/grantUser/{userId}")
    public ApiResponse grantUserRole(@PathVariable("userId") Long userId) {
        userService.grantUserRole(userId);
        return ApiResponse.ok();

    }

    // 닉네임 중복 체크
    @GetMapping("/checkNickname")
    public ApiResponse<Boolean> checkDuplicateNickname(@RequestParam("nickname") String nickname) {

        boolean checkDuplicateNickname = authService.checkDuplicateNickname(nickname);

        return ApiResponse.ok(checkDuplicateNickname);
    }

    //TODO 로그아웃하면 토큰 블랙리스트 처리? 혹은 다른 방법
}
