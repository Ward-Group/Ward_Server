package com.ward.ward_server.api.user.service;

import com.ward.ward_server.api.user.entity.SocialLogin;
import com.ward.ward_server.api.user.entity.User;
import com.ward.ward_server.api.user.repository.RefreshTokenRepository;
import com.ward.ward_server.api.user.repository.SocialLoginRepository;
import com.ward.ward_server.api.user.repository.UserRepository;
import com.ward.ward_server.global.exception.ApiException;
import com.ward.ward_server.global.exception.ExceptionCode;
import com.ward.ward_server.global.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final SocialLoginRepository socialLoginRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RestTemplate restTemplate;

    private final String KAKAO_UNLINK_URL = "https://kapi.kakao.com/v1/user/unlink";
    private final String APPLE_UNLINK_URL = "https://appleid.apple.com/auth/revoke";
    private final String APPLE_CLIENT_ID = "your-apple-client-id";
    private final String APPLE_CLIENT_SECRET = "your-apple-client-secret";

    public String getNickname(Long userId) {
        return userRepository.findNicknameById(userId)
                .orElseThrow(() -> new ApiException(ExceptionCode.USER_NOT_FOUND));
    }

    @Transactional
    public void updateNickname(Long userId, String newNickname) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ExceptionCode.USER_NOT_FOUND));
        boolean isNicknameDuplicate = checkDuplicateNickname(newNickname);
        ValidationUtils.validateNickname(newNickname, isNicknameDuplicate);
        user.changeNickname(newNickname, isNicknameDuplicate);
    }

    public boolean checkDuplicateNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionCode.USER_NOT_FOUND));

        // 소셜 로그인 정보 삭제 (카카오)
        disconnectKakao(user);

        // 소셜 로그인 정보 삭제 (애플)
        disconnectApple(user);

        // 리프레시 토큰 삭제
        refreshTokenRepository.deleteByUser(user);

        // 유저 데이터 삭제 (Cascade 설정에 따라 연관 데이터도 삭제됨)
        userRepository.delete(user);
    }

    private void disconnectKakao(User user) {
        Optional<SocialLogin> socialLogin = socialLoginRepository.findByUserAndProvider(user, "kakao");
        if (socialLogin.isPresent()) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + socialLogin.get().getProviderId());
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(KAKAO_UNLINK_URL, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() != HttpStatus.OK) {
                throw new ApiException(ExceptionCode.SOCIAL_DISCONNECT_FAILED, "카카오 계정 연동 해제 실패");
            }

            socialLoginRepository.delete(socialLogin.get());
        }
    }

    private void disconnectApple(User user) {
        Optional<SocialLogin> socialLogin = socialLoginRepository.findByUserAndProvider(user, "apple");
        if (socialLogin.isPresent()) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            String body = "client_id=" + APPLE_CLIENT_ID +
                    "&client_secret=" + APPLE_CLIENT_SECRET +
                    "&token=" + socialLogin.get().getProviderId();
            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(APPLE_UNLINK_URL, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() != HttpStatus.OK) {
                throw new ApiException(ExceptionCode.SOCIAL_DISCONNECT_FAILED, "애플 계정 연동 해제 실패");
            }

            socialLoginRepository.delete(socialLogin.get());
        }
    }
}
