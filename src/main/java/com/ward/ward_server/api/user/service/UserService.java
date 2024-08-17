package com.ward.ward_server.api.user.service;

import com.ward.ward_server.api.entry.entity.EntryRecord;
import com.ward.ward_server.api.entry.repository.EntryRecordRepository;
import com.ward.ward_server.api.user.entity.RefreshToken;
import com.ward.ward_server.api.user.entity.SocialLogin;
import com.ward.ward_server.api.user.entity.User;
import com.ward.ward_server.api.user.repository.RefreshTokenRepository;
import com.ward.ward_server.api.user.repository.SocialLoginRepository;
import com.ward.ward_server.api.user.repository.UserRepository;
import com.ward.ward_server.api.wishBrand.WishBrand;
import com.ward.ward_server.api.wishBrand.repository.WishBrandRepository;
import com.ward.ward_server.api.wishItem.WishItem;
import com.ward.ward_server.api.wishItem.repository.WishItemRepository;
import com.ward.ward_server.global.Object.Constants;
import com.ward.ward_server.global.exception.ApiException;
import com.ward.ward_server.global.exception.ExceptionCode;
import com.ward.ward_server.global.util.AppleClientSecretGenerator;
import com.ward.ward_server.global.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String KAKAO_AUTH_PREFIX = "KakaoAK ";
    private static final String CLIENT_ID_KEY = "client_id=";
    private static final String CLIENT_SECRET_KEY = "client_secret=";
    private static final String TOKEN_KEY = "&token=";

    private final UserRepository userRepository;
    private final EntryRecordRepository entryRecordRepository;
    private final WishItemRepository wishItemRepository;
    private final WishBrandRepository wishBrandRepository;
    private final SocialLoginRepository socialLoginRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RestTemplate restTemplate;

    @Value("${kakao.unlink-url}")
    private String kakaoUnlinkUrl;

    @Value("${kakao.service-app-admin-key}")
    private String serviceAppAdminKey;

    @Value("${apple.unlink-url}")
    private String appleUnlinkUrl;

    @Value("${apple.client-id}")
    private String appleClientId;

    @Value("${apple.key-id}")
    private String appleKeyId;

    @Value("${apple.team-id}")
    private String appleTeamId;

    @Value("${apple.private-key}")
    private String applePrivateKey;

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

        List<EntryRecord> entryRecords = entryRecordRepository.findByUserId(userId);
        List<WishItem> wishItems = wishItemRepository.findByUserId(userId);
        List<WishBrand> wishBrands = wishBrandRepository.findByUserId(userId);
        List<RefreshToken> refreshTokens = refreshTokenRepository.findByUserId(userId);

        try {
            disconnectKakao(user);

            disconnectApple(user);

            // 연관 데이터 일괄 삭제
            userRepository.deleteRefreshTokensByUserId(userId);
            userRepository.deleteWishItemsByUserId(userId);
            userRepository.deleteWishBrandsByUserId(userId);
            userRepository.deleteEntryRecordsByUserId(userId);

            // 유저 데이터 삭제
            userRepository.delete(user);
        } catch (ApiException e) {
            log.error("유저 삭제 중 오류 발생: {}", e.getMessage());
            throw e;
        }
    }

    private void disconnectKakao(User user) {
        Optional<SocialLogin> socialLogin = socialLoginRepository.findByUserAndProvider(user, Constants.KAKAO);
        if (socialLogin.isPresent()) {
            try {
                long providerId = Long.parseLong(socialLogin.get().getProviderId());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                headers.set(AUTHORIZATION_HEADER, KAKAO_AUTH_PREFIX + serviceAppAdminKey);

                String body = "target_id_type=user_id&target_id=" + providerId;
                HttpEntity<String> entity = new HttpEntity<>(body, headers);

                ResponseEntity<String> response = restTemplate.exchange(kakaoUnlinkUrl, HttpMethod.POST, entity, String.class);

                if (response.getStatusCode() != HttpStatus.OK) {
                    log.error("[UserService] 카카오 계정 연동 해제 실패. HTTP 상태 코드: {}, 사용자 ID: {}", response.getStatusCode(), user.getId());
                    throw new ApiException(ExceptionCode.SOCIAL_DISCONNECT_FAILED, "카카오 계정 연동 해제 실패");
                }

                socialLoginRepository.delete(socialLogin.get());
            } catch (NumberFormatException e) {
                log.error("[UserService] 유효하지 않은 카카오 providerId입니다. 사용자 ID: {}, providerId: {}", user.getId(), socialLogin.get().getProviderId());
                throw new ApiException(ExceptionCode.INVALID_PROVIDER_ID, "유효하지 않은 카카오 providerId입니다.");
            }
        }
    }

    private void disconnectApple(User user) {
        Optional<SocialLogin> socialLogin = socialLoginRepository.findByUserAndProvider(user, Constants.APPLE);
        if (socialLogin.isPresent()) {
            try {
                String appleRefreshToken = socialLogin.get().getAppleRefreshToken();
                if (appleRefreshToken == null || appleRefreshToken.isBlank()) {
                    log.error("[UserService] 애플 리프레시 토큰이 없습니다. 사용자 ID: {}", user.getId());
                    throw new ApiException(ExceptionCode.MISSING_REFRESH_TOKEN);
                }

                String clientSecret = AppleClientSecretGenerator.generateClientSecret(appleTeamId, appleClientId, appleKeyId, applePrivateKey);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                String body = CLIENT_ID_KEY + appleClientId +
                        CLIENT_SECRET_KEY + clientSecret +
                        TOKEN_KEY + appleRefreshToken;
                HttpEntity<String> entity = new HttpEntity<>(body, headers);

                ResponseEntity<String> response = restTemplate.exchange(appleUnlinkUrl, HttpMethod.POST, entity, String.class);

                if (response.getStatusCode() != HttpStatus.OK) {
                    log.error("[UserService] 애플 계정 연동 해제 실패. HTTP 상태 코드: {}, 사용자 ID: {}", response.getStatusCode(), user.getId());
                    throw new ApiException(ExceptionCode.SOCIAL_DISCONNECT_FAILED, "애플 계정 연동 해제 실패");
                }

                socialLoginRepository.delete(socialLogin.get());
            } catch (Exception e) {
                log.error("[UserService] 애플 계정 연동 해제 중 오류 발생. 사용자 ID: {}, 오류 메시지: {}", user.getId(), e.getMessage());
                throw new ApiException(ExceptionCode.SERVER_ERROR, "애플 계정 연동 해제 중 오류 발생");
            }
        }
    }
}
