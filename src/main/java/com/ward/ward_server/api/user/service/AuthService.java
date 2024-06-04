package com.ward.ward_server.api.user.service;

import com.ward.ward_server.api.user.auth.security.CustomUserDetails;
import com.ward.ward_server.api.user.auth.security.JwtIssuer;
import com.ward.ward_server.api.user.auth.security.JwtProperties;
import com.ward.ward_server.api.user.auth.security.JwtTokens;
import com.ward.ward_server.api.user.dto.LoginRequest;
import com.ward.ward_server.api.user.entity.SocialLogin;
import com.ward.ward_server.api.user.entity.User;
import com.ward.ward_server.api.user.repository.SocialLoginRepository;
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
    private final SocialLoginRepository socialLoginRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtIssuer jwtIssuer;
    private final AuthenticationManager authenticationManager;
    private final JwtProperties properties;
    private final RefreshTokenService refreshTokenService;

    // sociallogin.email 이 있으면 이미 가입한 회원
    public boolean isRegisteredUser(String email) {
        return socialLoginRepository.existsByEmail(email);
    }

    // provider+providerId 까지 동일하면 로그인 시켜야함.
    // 없으면 소셜 로그인 추가또는 기존 로그인으로 로그인
    public boolean isSameUser(String provider, String providerId) {
        return socialLoginRepository.existsByProviderAndProviderId(provider, providerId);
    }

    // 소셜 로그인 정보로 사용자 조회
    @Transactional
    public JwtTokens login(LoginRequest request) {
        if (!isRegisteredUser(request.getEmail())) {
            throw new ApiException(ExceptionCode.NON_EXISTENT_USER);
        }

        if (!isSameUser(request.getProvider(), request.getProviderId())) {
            throw new ApiException(ExceptionCode.EXISTENT_USER);
        }

        Optional<SocialLogin> socialLoginOptional = socialLoginRepository.findByProviderAndProviderIdAndEmail(request.getProvider(), request.getProviderId(), request.getEmail());
        if (socialLoginOptional.isEmpty()) {
            throw new ApiException(ExceptionCode.NON_EXISTENT_EMAIL);
        }

        User user = socialLoginOptional.get().getUser();
        return generateJwtTokens(user);
    }

    // 새로운 소셜 로그인 추가: 로그인 시 email 같고 provider+providerId 없는 경우
    @Transactional
    public JwtTokens addSocialLogin(String provider, String providerId, String email) {

        if (!ValidationUtil.isValidEmail(email)) {
            throw new ApiException(ExceptionCode.INVALID_EMAIL_FORMAT);
        }

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new ApiException(ExceptionCode.NON_EXISTENT_EMAIL);
        }

        if (isSameUser(provider, providerId)) {
            throw new ApiException(ExceptionCode.EXISTENT_USER_AT_ADD_SOCIAL_ACCOUNT);
        }

        User user = userOptional.get();
        updateSocialLogin(user, provider, providerId, email);

        return generateJwtTokens(user);
    }

    // 회원가입
    @Transactional
    public JwtTokens registerUser(
            String provider,
            String providerId,
            String name,
            String email,
            String nickname,
            Boolean emailNotification,
            Boolean appPushNotification,
            Boolean snsNotification
    ) {
        try {
            if (!ValidationUtil.isValidEmail(email)) {
                throw new ApiException(ExceptionCode.INVALID_EMAIL_FORMAT);
            }

            if (isRegisteredUser(email)) {
                throw new ApiException(ExceptionCode.EXISTENT_USER_AT_REGISTER);
            }

            if (isSameUser(provider, providerId)) {
                throw new ApiException(ExceptionCode.EXISTENT_USER_AT_REGISTER_WITH_PROVIDER_PID);
            }

            if (userRepository.existsByNickname(nickname)) {
                throw new ApiException(ExceptionCode.DUPLICATE_NICKNAME);
            }

            User user = new User(
                    name,
                    email,
                    passwordEncoder.encode(properties.getPassword()),
                    nickname,
                    emailNotification,
                    appPushNotification,
                    snsNotification
            );

            SocialLogin socialLogin = new SocialLogin(provider, providerId, email);
            socialLogin.setUser(user);

            socialLoginRepository.save(socialLogin);
            userRepository.save(user);

            return generateJwtTokens(user);
        } catch (DataIntegrityViolationException e) {
            log.error("회원 가입 중 데이터 무결성 위반 에러: ", e);
            throw new ApiException(ExceptionCode.REGISTRATION_ERROR_MESSAGE);
        } catch (ApiException e) {
            log.error("회원 가입 중 에러: {}", e.getMessage());
            throw e;
        }
    }

    // Refresh Token 갱신
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

    // RefreshToken 무효화 - 로그아웃 or 보안 상
    @Transactional
    public void invalidateRefreshToken(String refreshToken) {
        refreshTokenService.invalidateRefreshToken(refreshToken);
    }

    // 닉네임 중복 검사
    public boolean checkDuplicateNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    // 사용자의 소셜 로그인 정보 업데이트
    private void updateSocialLogin(User user, String provider, String providerId, String email) {
        Optional<SocialLogin> socialLoginOptional = socialLoginRepository.findByProviderAndProviderIdAndEmail(provider, providerId, email);

        if (socialLoginOptional.isPresent()) {
            SocialLogin socialLogin = socialLoginOptional.get();
            socialLogin.setUser(user);
        } else {
            SocialLogin socialLogin = new SocialLogin(provider, providerId, email);
            socialLogin.setUser(user);
            socialLoginRepository.save(socialLogin);
        }
    }

    // JWT 토큰 생성
    private JwtTokens generateJwtTokens(User user) {
        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), properties.getPassword())
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
}
