package com.ward.ward_server.api.user.controller;

import com.ward.ward_server.api.user.auth.security.CustomUserDetails;
import com.ward.ward_server.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HelloController {

    @GetMapping("/")
    public ApiResponse greeting(){
        String helloWorld = "Hello, World";
        return ApiResponse.ok(helloWorld);
    }

    @GetMapping("/user/secured")
    public ApiResponse secured(@AuthenticationPrincipal CustomUserDetails principal) {
        String secured = "If you see this, then you're logged in as user " + principal.getEmail()
                + " User ID: " + principal.getUserId();
        return ApiResponse.ok(secured);
    }

    @GetMapping("/admin")
    public ApiResponse admin(@AuthenticationPrincipal CustomUserDetails principal) {
        String admin = "If you see this, then you are an Admin. User ID: " + principal.getUserId();
        return ApiResponse.ok(admin);
    }
}
