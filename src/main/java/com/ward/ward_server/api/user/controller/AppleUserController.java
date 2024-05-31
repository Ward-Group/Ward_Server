package com.ward.ward_server.api.user.controller;

import com.ward.ward_server.api.user.dto.UserInfoUpdateRequest;
import com.ward.ward_server.api.user.service.AppleUserService;
import com.ward.ward_server.global.response.ApiResponse;
import com.ward.ward_server.global.response.ApiResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apple")
public class AppleUserController {

    private final AppleUserService appleUserService;

    @PostMapping("/updateUserInfo")
    public ApiResponse<Void> updateUserInfo(@RequestBody UserInfoUpdateRequest request) {
        appleUserService.updateUserInfo(request.getProvider(), request.getProviderId(), request.getOldEmail(), request.getNewEmail());
        return ApiResponse.ok(ApiResponseMessage.UPDATE_SOCIALLOGIN_SUCCESS);
    }
}
