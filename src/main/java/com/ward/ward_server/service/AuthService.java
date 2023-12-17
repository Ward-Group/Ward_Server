package com.ward.ward_server.service;

import com.ward.ward_server.entity.UserEntity;
import com.ward.ward_server.model.LoginResponse;
import com.ward.ward_server.model.RegisterErrorResponse;
import com.ward.ward_server.model.RegisterRequest;
import com.ward.ward_server.model.RegisterSuccessResponse;
import com.ward.ward_server.repository.UserRepository;
import com.ward.ward_server.security.JwtIssuer;
import com.ward.ward_server.security.UserPrincipal;
import com.ward.ward_server.util.Constants;
import com.ward.ward_server.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtIssuer jwtIssuer;
    private final AuthenticationManager authenticationManager;

    public LoginResponse attemptLogin(String email, String password) {
        var authentication = authenticationManager.authenticate( // 사용자의 인증을 시도하고, 인증이 성공하면 Authentication 객체를 반환, 이 객체에는 사용자 정보와 권한이 포함
                new UsernamePasswordAuthenticationToken(email, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication); // 현재 사용자의 인증 정보를 설정
        var principal = (UserPrincipal) authentication.getPrincipal(); // 현재 사용자의 UserPrincipal을 가져옵니다
        log.info(principal.toString());
        var roles = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        var token = jwtIssuer.issue(principal.getUserId(), principal.getEmail(), roles);
        return LoginResponse.builder()
                .accessToken(token)
                .build();
    }

    public Object registerUser(RegisterRequest request) {
        if (!ValidationUtil.isValidEmail(request.getEmail())) {
            return RegisterErrorResponse.builder()
                    .status(400)
                    .success(false)
                    .message(Constants.INVALID_EMAIL_FORMAT)
                    .build();
        }

        if (!ValidationUtil.isValidPassword(request.getPassword())) {
            return RegisterErrorResponse.builder()
                    .status(400)
                    .success(false)
                    .message(Constants.INVALID_PASSWORD_FORMAT)
                    .build();
        }

        if (!userRepository.findByEmail(request.getEmail()).isEmpty()) {
            return RegisterErrorResponse.builder()
                    .status(400)
                    .success(false)
                    .message(Constants.EMAIL_ALREADY_EXISTS)
                    .build();
        }

        UserEntity newUser = new UserEntity();
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole("ROLE_USER");

        userRepository.save(newUser);

        return RegisterSuccessResponse.builder()
                .status(200)
                .success(true)
                .message(Constants.SUCCESSFUL_REGISTRATION)
                .user(RegisterSuccessResponse.UserResponse.builder()
                        .userId(newUser.getId())
                        .email(newUser.getEmail())
                        .role(newUser.getRole())
                        .build())
                .build();
    }

}
