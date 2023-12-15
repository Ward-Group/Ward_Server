package com.ward.ward_server.service;

import com.ward.ward_server.model.LoginResponse;
import com.ward.ward_server.security.JwtIssuer;
import com.ward.ward_server.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtIssuer jwtIssuer;
    private final AuthenticationManager authenticationManager;

    public LoginResponse attemptLogin(String email, String password) {
        var authentication = authenticationManager.authenticate( // 사용자의 인증을 시도하고, 인증이 성공하면 Authentication 객체를 반환, 이 객체에는 사용자 정보와 권한이 포함
                new UsernamePasswordAuthenticationToken(email, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication); // 현재 사용자의 인증 정보를 설정
        var principal = (UserPrincipal) authentication.getPrincipal(); // 현재 사용자의 UserPrincipal을 가져옵니다

        var roles = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        var token = jwtIssuer.issue(principal.getUserId(), principal.getEmail(), roles);
        return LoginResponse.builder()
                .accessToken(token)
                .build();
    }
}
