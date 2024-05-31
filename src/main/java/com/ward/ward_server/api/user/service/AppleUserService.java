package com.ward.ward_server.api.user.service;

import com.ward.ward_server.api.user.entity.SocialLogin;
import com.ward.ward_server.api.user.entity.User;
import com.ward.ward_server.api.user.repository.SocialLoginRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AppleUserService {

    private final SocialLoginRepository socialLoginRepository;

    @Transactional
    public void updateUserInfo(String provider, String providerId, String oldEmail, String newEmail) {
        // SocialLogin 테이블에서 사용자 정보를 찾습니다.
        Optional<SocialLogin> optionalSocialLogin = socialLoginRepository.findByProviderAndProviderIdAndEmail(provider, providerId, oldEmail);

        if (optionalSocialLogin.isPresent()) {
            // SocialLogin 정보를 통해 사용자 정보를 가져옵니다.
            SocialLogin socialLogin = optionalSocialLogin.get();
            User user = socialLogin.getUser();

            // 사용자 이메일 업데이트
            user.updateEmail(newEmail);
            log.info("사용자 정보 업데이트: {}", user);

            // 소셜 로그인 정보 이메일 업데이트
            socialLogin.updateEmail(newEmail);
        } else {
            // SocialLogin 정보가 존재하지 않으면 로깅하여 알립니다.
            log.warn("해당 이메일을 가진 사용자를 찾을 수 없습니다: {}", oldEmail);
        }
    }
}
