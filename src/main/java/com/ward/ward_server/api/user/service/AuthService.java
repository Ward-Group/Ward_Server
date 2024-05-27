package com.ward.ward_server.api.user.service;

import com.ward.ward_server.api.user.auth.security.CustomUserDetails;
import com.ward.ward_server.api.user.auth.security.JwtIssuer;
import com.ward.ward_server.api.user.auth.security.JwtProperties;
import com.ward.ward_server.api.user.auth.security.JwtTokens;
import com.ward.ward_server.api.user.dto.RegisterRequest;
import com.ward.ward_server.api.user.entity.User;
import com.ward.ward_server.api.user.entity.enumtype.Role;
import com.ward.ward_server.api.user.repository.UserRepository;
import com.ward.ward_server.global.exception.ApiException;
import com.ward.ward_server.global.exception.ExceptionCode;
import com.ward.ward_server.global.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
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
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public JwtTokens attemptLogin(String provider, String providerId, String email, String password) {
        String username = provider + providerId;
        log.info("[Slf4j]Username: " + username);

        if (userRepository.findByEmail(email).isEmpty()) {
            throw new ApiException(ExceptionCode.NON_EXISTENT_EMAIL);
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

            User user = userRepository.findById(principal.getUserId())
                    .orElseThrow(() -> new ApiException(ExceptionCode.USER_NOT_FOUND));
            refreshTokenService.saveRefreshToken(user, refreshToken);

            return new JwtTokens(accessToken, refreshToken);
        } catch (AuthenticationException e) {
            log.error("Login failed: ", e);
            throw new ApiException(ExceptionCode.INVALID_USERNAME_OR_PASSWORD);
        }
    }

    @Transactional
    public JwtTokens refresh(String refreshToken) {
        var refreshTokenEntity = refreshTokenService.findRefreshTokenByToken(refreshToken);
        var user = refreshTokenEntity.getUser();

        var roles = user.getRole().toString();
        var newAccessToken = jwtIssuer.issueAccessToken(user.getId(), user.getEmail(), List.of(roles));
        var newRefreshToken = jwtIssuer.issueRefreshToken();

        refreshTokenService.invalidateAndSaveNewToken(refreshTokenEntity, newRefreshToken);

        return new JwtTokens(newAccessToken, newRefreshToken);
    }

    @Transactional
    public JwtTokens registerUser(RegisterRequest request) {
        try {
            if (!ValidationUtil.isValidEmail(request.getEmail())) {
                throw new ApiException(ExceptionCode.INVALID_EMAIL_FORMAT);
            }

            if (!ValidationUtil.isValidPassword(properties.getPassword())) {
                throw new ApiException(ExceptionCode.INVALID_PASSWORD_FORMAT);
            }

            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new ApiException(ExceptionCode.EMAIL_ALREADY_EXISTS);
            }

            if (checkDuplicateNickname(request.getNickname())) {
                throw new ApiException(ExceptionCode.DUPLICATE_NICKNAME);
            }

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

            userRepository.save(newUser);

            var roles = List.of(Role.ROLE_USER.toString());
            var accessToken = jwtIssuer.issueAccessToken(newUser.getId(), newUser.getEmail(), roles);
            var refreshToken = jwtIssuer.issueRefreshToken();

            refreshTokenService.saveRefreshToken(newUser, refreshToken);

            return new JwtTokens(accessToken, refreshToken);
        } catch (DataIntegrityViolationException e) {
            log.error("Error during user registration:", e);
            throw new ApiException(ExceptionCode.REGISTRATION_ERROR_MESSAGE);
        }
    }

    @Transactional
    public void invalidateRefreshToken(String refreshToken) {
        refreshTokenService.invalidateRefreshToken(refreshToken);
    }

    public boolean checkDuplicateNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}
