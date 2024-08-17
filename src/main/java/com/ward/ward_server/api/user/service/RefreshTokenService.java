package com.ward.ward_server.api.user.service;

import com.ward.ward_server.api.user.entity.RefreshToken;
import com.ward.ward_server.api.user.entity.User;
import com.ward.ward_server.api.user.repository.RefreshTokenRepository;
import com.ward.ward_server.global.exception.ApiException;
import com.ward.ward_server.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void saveNewRefreshToken(User user, String refreshToken) {
        var refreshTokenEntity = new RefreshToken(refreshToken, user);
        refreshTokenRepository.save(refreshTokenEntity);
    }

    @Transactional
    public void invalidateRefreshToken(String refreshToken) {
        var token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new ApiException(ExceptionCode.INVALID_REFRESH_TOKEN));
        refreshTokenRepository.delete(token);
    }

    @Transactional
    public RefreshToken findRefreshTokenByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ApiException(ExceptionCode.INVALID_REFRESH_TOKEN));
    }

    @Transactional
    public void invalidateAndSaveNewToken(RefreshToken oldToken, String newToken) {
        refreshTokenRepository.delete(oldToken);
        saveNewRefreshToken(oldToken.getUser(), newToken);
    }
}
