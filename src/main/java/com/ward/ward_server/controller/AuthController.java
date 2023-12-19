package com.ward.ward_server.controller;

import com.ward.ward_server.model.LoginRequest;
import com.ward.ward_server.model.RegisterRequest;
import com.ward.ward_server.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/auth/login")
    public Object login(@RequestBody @Validated LoginRequest request){
        return authService.attemptLogin(request.getEmail(), request.getPassword());
    }

    @PostMapping("/auth/registerUser")
    public Object register(@RequestBody @Validated RegisterRequest request) {
        return authService.registerUser(request);
    }
}
