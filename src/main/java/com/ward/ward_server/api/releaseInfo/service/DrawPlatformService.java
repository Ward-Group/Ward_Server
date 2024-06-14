package com.ward.ward_server.api.releaseInfo.service;

import com.ward.ward_server.api.releaseInfo.entity.DrawPlatform;
import com.ward.ward_server.api.releaseInfo.repository.DrawPlatformRepository;
import com.ward.ward_server.global.exception.ApiException;
import com.ward.ward_server.global.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import static com.ward.ward_server.global.exception.ExceptionCode.*;
import static com.ward.ward_server.global.response.error.ErrorCode.NAME_MUST_BE_PROVIDED;

@Service
@RequiredArgsConstructor
public class DrawPlatformService {
    private final DrawPlatformRepository drawPlatformRepository;

    @Transactional
    public DrawPlatform createDrawPlatform(String koreanName, String englishName, String logoImage) {
        if (!StringUtils.hasText(koreanName) && !StringUtils.hasText(englishName))
            throw new ApiException(INVALID_INPUT, NAME_MUST_BE_PROVIDED.getMessage());
        ValidationUtils.validationNames(koreanName, englishName);
        if (drawPlatformRepository.existsByKoreanNameOrEnglishName(koreanName, englishName))
            throw new ApiException(DUPLICATE_DRAW_PLATFORM_NAME);

        return drawPlatformRepository.save(
                DrawPlatform.builder()
                        .koreanName(koreanName)
                        .englishName(englishName)
                        .logoImage(logoImage)
                        .build());
    }

    @Transactional
    public DrawPlatform updateDrawPlatform(String originName, String koreanName, String englishName, String logoImage) {
        DrawPlatform origin = drawPlatformRepository.findByName(originName)
                .orElseThrow(() -> new ApiException(DRAW_PLATFORM_NOT_FOUND));
        ValidationUtils.validationNames(koreanName, englishName);
        if (drawPlatformRepository.existsByKoreanNameOrEnglishName(koreanName, englishName))
            throw new ApiException(DUPLICATE_DRAW_PLATFORM_NAME);

        if (StringUtils.hasText(koreanName)) origin.updateKoreanName(koreanName);
        if (StringUtils.hasText(englishName)) origin.updateEnglishName(englishName);
        if (StringUtils.hasText(logoImage)) origin.updateLogoImage(logoImage);
        return origin;
    }

    @Transactional
    public void deleteDrawPlatform(String originName) {
        DrawPlatform drawPlatform = drawPlatformRepository.findByName(originName)
                .orElseThrow(() -> new ApiException(DRAW_PLATFORM_NOT_FOUND));
        drawPlatformRepository.delete(drawPlatform);
    }

}
