package com.ward.ward_server.api.user.controller;

import com.ward.ward_server.api.user.service.UserService;
import com.ward.ward_server.global.response.ApiResponse;
import com.ward.ward_server.global.response.ApiResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/user")
public class PublicUserController {

    private final UserService userService;

    @GetMapping("/check-nickname")
    public ApiResponse<Boolean> checkDuplicateNickname(@RequestParam("nickname") String nickname) {
        boolean isDuplicate = userService.checkDuplicateNickname(nickname);
        return ApiResponse.ok(ApiResponseMessage.NICKNAME_CHECK_SUCCESS, isDuplicate);
    }
}
