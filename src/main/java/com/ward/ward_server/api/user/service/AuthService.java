package com.ward.ward_server.api.user.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ward.ward_server.api.user.auth.security.*;
import com.ward.ward_server.api.user.dto.LoginRequest;
import com.ward.ward_server.api.user.entity.SocialLogin;
import com.ward.ward_server.api.user.entity.User;
import com.ward.ward_server.api.user.repository.SocialLoginRepository;
import com.ward.ward_server.api.user.repository.UserRepository;
import com.ward.ward_server.global.Object.Constants;
import com.ward.ward_server.global.exception.ApiException;
import com.ward.ward_server.global.exception.ExceptionCode;
import com.ward.ward_server.global.util.ValidationUtils;
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
    private final JwtDecoder jwtDecoder;

    public boolean isRegisteredUser(String email) {
        return socialLoginRepository.existsByEmail(email);
    }

    public Optional<SocialLogin> getSocialLoginByEmail(String email) {
        return socialLoginRepository.findByEmail(email);
    }

    public boolean isSameUser(String provider, String providerId) {
        return socialLoginRepository.existsByProviderAndProviderId(provider, providerId);
    }

    @Transactional
    public JwtTokens login(LoginRequest request) {
        Optional<SocialLogin> socialLoginOptional;

        if (request.email() != null && !request.email().isBlank()) {
            socialLoginOptional = socialLoginRepository.findByProviderAndProviderIdAndEmail(request.provider(), request.providerId(), request.email());
        } else {
            socialLoginOptional = socialLoginRepository.findByProviderAndProviderId(request.provider(), request.providerId());
        }

        if (socialLoginOptional.isEmpty()) {
            Optional<SocialLogin> existingSocialLoginOptional = getSocialLoginByEmail(request.email());

            if (existingSocialLoginOptional.isPresent()) {
                SocialLogin existingSocialLogin = existingSocialLoginOptional.get();
                String provider = existingSocialLogin.getProvider();

                if (Constants.KAKAO.equalsIgnoreCase(provider)) {
                    throw new ApiException(ExceptionCode.EXISTENT_USER_KAKAO);
                } else if (Constants.APPLE.equalsIgnoreCase(provider)) {
                    throw new ApiException(ExceptionCode.EXISTENT_USER_APPLE);
                } else {
                    throw new ApiException(ExceptionCode.EXISTENT_USER);
                }
            }
            throw new ApiException(ExceptionCode.NON_EXISTENT_USER);
        }

        User user = socialLoginOptional.get().getUser();
        return generateJwtTokens(user);
    }

    @Transactional
    public JwtTokens addSocialLogin(String provider, String providerId, String email, String appleRefreshToken) {

        if (!ValidationUtils.isValidEmail(email)) {
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
        updateSocialLogin(user, provider, providerId, email, appleRefreshToken);

        return generateJwtTokens(user);
    }

    @Transactional
    public JwtTokens registerUser(
            String provider,
            String providerId,
            String name,
            String email,
            String appleRefreshToken,
            String nickname,
            Boolean emailNotification,
            Boolean appPushNotification,
            Boolean snsNotification
    ) {
        try {
            if (!ValidationUtils.isValidEmail(email)) {
                throw new ApiException(ExceptionCode.INVALID_EMAIL_FORMAT);
            }

            if (isRegisteredUser(email)) {
                throw new ApiException(ExceptionCode.EXISTENT_USER_AT_REGISTER);
            }

            if (isSameUser(provider, providerId)) {
                throw new ApiException(ExceptionCode.EXISTENT_USER_AT_REGISTER_WITH_PROVIDER_PID);
            }

            boolean isNicknameDuplicate = userRepository.existsByNickname(nickname);
            ValidationUtils.validateNickname(nickname, isNicknameDuplicate);

            User user = new User(
                    name,
                    email,
                    passwordEncoder.encode(properties.getPassword()),
                    nickname,
                    emailNotification,
                    appPushNotification,
                    snsNotification
            );
            SocialLogin socialLogin = new SocialLogin(provider, providerId, email, appleRefreshToken);
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
        } catch (Exception e) {
            log.error("회원 가입 중 예상치 못한 오류 발생", e);
            throw new ApiException(ExceptionCode.SERVER_ERROR);
        }
    }

    @Transactional
    public JwtTokens refresh(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            log.error("리프레시 토큰이 누락되었습니다.");
            throw new ApiException(ExceptionCode.MISSING_REFRESH_TOKEN);
        }

        try {
            DecodedJWT decodedJWT = jwtDecoder.decode(refreshToken);
            if (jwtDecoder.isTokenExpired(decodedJWT)) {
                log.error("리프레시 토큰이 만료되었습니다: {}", refreshToken);
                throw new ApiException(ExceptionCode.TOKEN_EXPIRED);
            }

            var refreshTokenEntity = refreshTokenService.findRefreshTokenByToken(refreshToken);
            if (refreshTokenEntity == null) {
                log.error("유효하지 않은 리프레시 토큰: {}", refreshToken);
                throw new ApiException(ExceptionCode.INVALID_REFRESH_TOKEN);
            }
            var user = refreshTokenEntity.getUser();

            var roles = user.getRole().toString();
            var newAccessToken = jwtIssuer.issueAccessToken(user.getId(), user.getEmail(), List.of(roles));
            var newRefreshToken = jwtIssuer.issueRefreshToken();

            refreshTokenService.invalidateAndSaveNewToken(refreshTokenEntity, newRefreshToken);

            return new JwtTokens(newAccessToken, newRefreshToken);
        } catch (ApiException e) {
            log.error("토큰 갱신 중 API 예외 발생: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("토큰 갱신 중 예상치 못한 오류 발생", e);
            throw new ApiException(ExceptionCode.SERVER_ERROR);
        }
    }


    @Transactional
    public void invalidateRefreshToken(String refreshToken) {
        refreshTokenService.invalidateRefreshToken(refreshToken);
    }

    private void updateSocialLogin(User user, String provider, String providerId, String email, String appleRefreshToken) {
        Optional<SocialLogin> socialLoginOptional = socialLoginRepository.findByProviderAndProviderIdAndEmail(provider, providerId, email);

        if (socialLoginOptional.isPresent()) {
            SocialLogin socialLogin = socialLoginOptional.get();
            socialLogin.setUser(user);
        } else {
            SocialLogin socialLogin = new SocialLogin(provider, providerId, email, appleRefreshToken);
            socialLogin.setUser(user);
            socialLoginRepository.save(socialLogin);
        }
    }

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

            refreshTokenService.saveNewRefreshToken(user, refreshToken);

            return new JwtTokens(accessToken, refreshToken);
        } catch (AuthenticationException e) {
            log.error("로그인 실패: ", e);
            throw new ApiException(ExceptionCode.INVALID_USERNAME_OR_PASSWORD);
        }
    }
}

