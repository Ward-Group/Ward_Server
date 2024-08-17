package com.ward.ward_server.api.user.service;

import com.ward.ward_server.api.user.entity.SocialLogin;
import com.ward.ward_server.api.user.repository.SocialLoginRepository;
import com.ward.ward_server.global.exception.ApiException;
import com.ward.ward_server.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AppleUserService {

    private final SocialLoginRepository socialLoginRepository;

    @Transactional
    public void updateUserInfo(String provider, String providerId, String oldEmail, String newEmail) {
        // SocialLogin 테이블에서 사용자 정보를 찾고, 없으면 예외를 던집니다.
        SocialLogin socialLogin = socialLoginRepository
                .findByProviderAndProviderIdAndEmail(provider, providerId, oldEmail)
                .orElseThrow(() -> {
                    log.warn("해당 이메일을 가진 사용자를 찾을 수 없습니다. Provider: {}, ProviderId: {}, OldEmail: {}", provider, providerId, oldEmail);
                    return new ApiException(ExceptionCode.NON_EXISTENT_EMAIL);
                });

        // 소셜 로그인 정보 이메일 업데이트
        socialLogin.updateEmail(newEmail);
        log.info("소셜 로그인 정보 업데이트: {}", socialLogin);
    }
}

