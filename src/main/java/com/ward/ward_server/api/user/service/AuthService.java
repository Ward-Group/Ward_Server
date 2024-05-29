package com.ward.ward_server.api.user.service;

import com.ward.ward_server.api.user.auth.security.CustomUserDetails;
import com.ward.ward_server.api.user.auth.security.JwtIssuer;
import com.ward.ward_server.api.user.auth.security.JwtProperties;
import com.ward.ward_server.api.user.auth.security.JwtTokens;
import com.ward.ward_server.api.user.dto.RegisterRequest;
import com.ward.ward_server.api.user.entity.SocialLogin;
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
import java.util.Optional;

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

    public boolean isRegisteredUser(String provider, String providerId, String email) {
        return userRepository.existsByEmailAndSocialLogins_ProviderAndSocialLogins_ProviderId(email, provider, providerId);
    }

    @Transactional
    public JwtTokens attemptLogin(String provider, String providerId, String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new ApiException(ExceptionCode.NON_EXISTENT_EMAIL);
        }

        User user = userOptional.get();

        // 소셜 로그인 정보 확인 및 추가
        if (user.getSocialLogins().stream().noneMatch(socialLogin ->
                socialLogin.getProvider().equals(provider) &&
                        socialLogin.getProviderId().equals(providerId))) {

            SocialLogin socialLogin = new SocialLogin(provider, providerId);
            user.addSocialLogin(socialLogin);
        }

        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            var principal = (CustomUserDetails) authentication.getPrincipal();
            log.info("[Slf4j] 로그인 CustomUserDetails: " + principal.toString());
            var roles = principal.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            var accessToken = jwtIssuer.issueAccessToken(principal.getUserId(), principal.getEmail(), roles);
            var refreshToken = jwtIssuer.issueRefreshToken();

            refreshTokenService.saveRefreshToken(user, refreshToken);

            return new JwtTokens(accessToken, refreshToken);
        } catch (AuthenticationException e) {
            log.error("로그인 실패: ", e);
            throw new ApiException(ExceptionCode.INVALID_USERNAME_OR_PASSWORD);
        }
    }


    @Transactional
    public JwtTokens registerUser(RegisterRequest request) {
        try {
            if (!ValidationUtil.isValidEmail(request.getEmail())) {
                throw new ApiException(ExceptionCode.INVALID_EMAIL_FORMAT);
            }

            Optional<User> existingUserByEmail = userRepository.findByEmail(request.getEmail());

            User user;
            if (existingUserByEmail.isPresent()) {
                user = existingUserByEmail.get();
                // 중복 소셜 로그인 정보가 없는 경우 provider+providerId 추가
                if (user.getSocialLogins().stream().noneMatch(socialLogin ->
                        socialLogin.getProvider().equals(request.getProvider()) &&
                                socialLogin.getProviderId().equals(request.getProviderId()))) {

                    SocialLogin socialLogin = new SocialLogin(request.getProvider(), request.getProviderId());
                    user.addSocialLogin(socialLogin);
                }
            } else {
                if (userRepository.existsByNickname(request.getNickname())) {
                    throw new ApiException(ExceptionCode.DUPLICATE_NICKNAME);
                }

                user = new User(
                        request.getName(),
                        request.getEmail(),
                        passwordEncoder.encode(properties.getPassword()),
                        request.getNickname(),
                        request.getEmailNotification(),
                        request.getAppPushNotification(),
                        request.getSnsNotification()
                );

                SocialLogin socialLogin = new SocialLogin(request.getProvider(), request.getProviderId());
                user.addSocialLogin(socialLogin);

                userRepository.save(user);
            }

            var roles = List.of(Role.ROLE_USER.toString());
            var accessToken = jwtIssuer.issueAccessToken(user.getId(), user.getEmail(), roles);
            var refreshToken = jwtIssuer.issueRefreshToken();

            refreshTokenService.saveRefreshToken(user, refreshToken);

            return new JwtTokens(accessToken, refreshToken);
        } catch (DataIntegrityViolationException e) {
            log.error("회원 가입 중 데이터 무결성 위반 에러: ", e);
            throw new ApiException(ExceptionCode.REGISTRATION_ERROR_MESSAGE);
        } catch (ApiException e) {
            log.error("회원 가입 중 에러: {}", e.getMessage());
            throw e;
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
    public void invalidateRefreshToken(String refreshToken) {
        refreshTokenService.invalidateRefreshToken(refreshToken);
    }

    public boolean checkDuplicateNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}
