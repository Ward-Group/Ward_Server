package com.ward.ward_server.api.user.service;

import com.ward.ward_server.api.user.auth.security.*;
import com.ward.ward_server.api.user.dto.RegisterRequest;
import com.ward.ward_server.api.user.entity.User;
import com.ward.ward_server.api.user.entity.enumtype.Role;
import com.ward.ward_server.api.user.repository.UserRepository;
import com.ward.ward_server.global.exception.ExceptionCode;
import com.ward.ward_server.global.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtIssuer jwtIssuer;
    private final AuthenticationManager authenticationManager;
    private final JwtProperties properties;

    // 로그인
    @Transactional
    public JwtTokens attemptLogin(String provider, String providerId, String email, String password) {
        String username = provider + providerId;
        log.info("[Slf4j]Username: " + username);

        // 존재하지 않는 이메일
        if (userRepository.findByEmail(email).isEmpty()) {
            throw new BadCredentialsException(ExceptionCode.NON_EXISTENT_EMAIL.getMessage());
        }

        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            var principal = (CustomUserDetails) authentication.getPrincipal();
            log.info("[Slf4j]로그인 CustomUserDetails: " + principal.toString());
            var roles = principal.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            var accessToken = jwtIssuer.issueAccessToken(principal.getUserId(), principal.getEmail(), roles);
            var refreshToken = jwtIssuer.issueRefreshToken();

            // Refresh Token 저장
            User user = userRepository.findById(principal.getUserId())
                    .orElseThrow(() -> new BadCredentialsException(ExceptionCode.USER_NOT_FOUND.getMessage()));
            user.updateRefreshToken(refreshToken);
            userRepository.save(user);

            return new JwtTokens(accessToken, refreshToken);
        } catch (BadCredentialsException e) {
            log.error("Login failed: ", e);
            throw new BadCredentialsException(ExceptionCode.INVALID_USERNAME_OR_PASSWORD.getMessage());
        } catch (AuthenticationException e) {
            log.error("Login failed: ", e);
            throw new RuntimeException(ExceptionCode.LOGIN_FAIL.getMessage());
        }
    }

    @Transactional
    public JwtTokens refresh(String refreshToken) {
        var user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new BadCredentialsException(ExceptionCode.USER_NOT_FOUND.getMessage()));

        var roles = user.getRole().toString();
        var newAccessToken = jwtIssuer.issueAccessToken(user.getId(), user.getEmail(), List.of(roles));
        var newRefreshToken = jwtIssuer.issueRefreshToken();

        user.updateRefreshToken(newRefreshToken);
        userRepository.save(user);

        return new JwtTokens(newAccessToken, newRefreshToken);
    }

    @Transactional
    public JwtTokens registerUser(RegisterRequest request) {
        try {
            // 이메일 유효성 검사
            if (!ValidationUtil.isValidEmail(request.getEmail())) {
                throw new BadCredentialsException(ExceptionCode.INVALID_EMAIL_FORMAT.getMessage());
            }

            // 비밀번호 유효성 검사
            if (!ValidationUtil.isValidPassword(properties.getPassword())) {
                throw new BadCredentialsException(ExceptionCode.INVALID_PASSWORD_FORMAT.getMessage());
            }

            // 중복된 이메일 체크
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new BadCredentialsException(ExceptionCode.EMAIL_ALREADY_EXISTS.getMessage());
            }

            // 중복된 닉네임 체크
            if (checkDuplicateNickname(request.getNickname())) {
                throw new BadCredentialsException(ExceptionCode.DUPLICATE_NICKNAME.getMessage());
            }

            // 회원 등록 시작
            String username = request.getProvider() + request.getProviderId();
            User newUser = new User(
                    username,
                    request.getName(),
                    request.getEmail(),
                    passwordEncoder.encode(properties.getPassword()),
                    request.getNickname(),
                    request.getEmailNotification(),
                    request.getAppPushNotification(),
                    request.getSnsNotification()
            );

            // 회원 정보 저장
            userRepository.save(newUser);

            // JWT 토큰 발급
            var roles = List.of(Role.ROLE_USER.toString());
            var accessToken = jwtIssuer.issueAccessToken(newUser.getId(), newUser.getEmail(), roles);
            var refreshToken = jwtIssuer.issueRefreshToken();

            newUser.updateRefreshToken(refreshToken);
            userRepository.save(newUser);

            return new JwtTokens(accessToken, refreshToken);
        } catch (DataIntegrityViolationException e) {
            // 데이터베이스 무결성과 관련된 예외 처리
            log.error("Error during user registration:", e);
            throw new RuntimeException(ExceptionCode.REGISTRATION_ERROR_MESSAGE.getMessage());
        }
    }

    // 닉네임 중복 체크
    public boolean checkDuplicateNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}
