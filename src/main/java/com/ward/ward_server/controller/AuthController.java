package com.ward.ward_server.controller;

import com.ward.ward_server.model.LoginRequest;
import com.ward.ward_server.model.LoginResponse;
import com.ward.ward_server.security.JwtIssuer;
import com.ward.ward_server.security.UserPrincipal;
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
    public LoginResponse login(@RequestBody @Validated LoginRequest request){
        return authService.attemtLogion(request.getEmail(), request.getPassword());
    }
}
