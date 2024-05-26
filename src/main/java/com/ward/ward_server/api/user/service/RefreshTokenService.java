package com.ward.ward_server.api.user.service;

import com.ward.ward_server.api.user.entity.RefreshToken;
import com.ward.ward_server.api.user.entity.User;
import com.ward.ward_server.api.user.repository.RefreshTokenRepository;
import com.ward.ward_server.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    // 토큰 생성
    @Transactional
    public void saveRefreshToken(User user, String refreshToken) {
        var refreshTokenEntity = new RefreshToken(refreshToken, user);
        refreshTokenRepository.save(refreshTokenEntity);
    }

    // 토큰 무효화
    @Transactional
    public void invalidateRefreshToken(String refreshToken) {
        var token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new BadCredentialsException(ExceptionCode.INVALID_REFRESH_TOKEN.getMessage()));
        token.invalidate();
        refreshTokenRepository.save(token);
    }

    @Transactional
    public RefreshToken findValidRefreshToken(String token) {
        return refreshTokenRepository.findByTokenAndInvalidFalse(token)
                .orElseThrow(() -> new BadCredentialsException(ExceptionCode.INVALID_REFRESH_TOKEN.getMessage()));
    }

    // 토큰 갱신
    @Transactional
    public void invalidateAndSaveNewToken(RefreshToken oldToken, String newToken) {
        oldToken.invalidate();
        refreshTokenRepository.save(oldToken);
        saveRefreshToken(oldToken.getUser(), newToken);
    }
}