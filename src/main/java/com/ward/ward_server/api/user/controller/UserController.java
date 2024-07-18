package com.ward.ward_server.api.user.controller;

import com.ward.ward_server.api.user.auth.security.CustomUserDetails;
import com.ward.ward_server.api.user.dto.UpdateNicknameRequest;
import com.ward.ward_server.api.user.service.UserService;
import com.ward.ward_server.global.response.ApiResponse;
import com.ward.ward_server.global.response.ApiResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/nickname")
    public ApiResponse<String> getNickname(@AuthenticationPrincipal CustomUserDetails principal) {
        String nickname = userService.getNickname(principal.getUserId());
        return ApiResponse.ok(nickname);
    }

    @PatchMapping("/nickname")
    public ApiResponse<Void> updateNickname(@AuthenticationPrincipal CustomUserDetails principal, @RequestBody UpdateNicknameRequest request) {
        userService.updateNickname(principal.getUserId(), request.newNickname());
        return ApiResponse.ok(ApiResponseMessage.UPDATE_NICKNAME_SUCCESS);
    }

    @DeleteMapping("/delete-account")
    public ApiResponse<Void> deleteUserAccount(@AuthenticationPrincipal CustomUserDetails principal) {
        userService.deleteUser(principal.getUserId());
        return ApiResponse.ok(ApiResponseMessage.ACCOUNT_DELETION_SUCCESS);
    }
}
