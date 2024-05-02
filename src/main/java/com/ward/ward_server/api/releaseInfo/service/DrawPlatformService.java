package com.ward.ward_server.api.releaseInfo.service;

import com.ward.ward_server.api.releaseInfo.dto.DrawPlatformRequest;
import com.ward.ward_server.api.releaseInfo.entity.DrawPlatform;
import com.ward.ward_server.api.releaseInfo.repository.DrawPlatformRepository;
import com.ward.ward_server.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ward.ward_server.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
public class DrawPlatformService {

    private final DrawPlatformRepository drawPlatformRepository;

    public DrawPlatform createDrawPlatform(DrawPlatformRequest request) {
        if ((request.name() != null && request.name().isBlank()) || (request.logoImage() != null && request.logoImage().isBlank()))
            throw new ApiException(INVALID_INPUT);
        if (drawPlatformRepository.existsByName(request.name())) throw new ApiException(DUPLICATE_DRAW_PLATFORM_NAME);
        return drawPlatformRepository.save(
                DrawPlatform.builder()
                        .name(request.name())
                        .logoImage(request.logoImage())
                        .build());
    }

    @Transactional
    public DrawPlatform updateDrawPlatform(String originName, DrawPlatformRequest request) {
        DrawPlatform drawPlatform = drawPlatformRepository.findByName(originName)
                .orElseThrow(() -> new ApiException(DRAW_PLATFORM_NOT_FOUND));
        if (request.name() != null && !request.name().isBlank()) drawPlatform.updateName(request.name());
        if (request.logoImage() != null && !request.logoImage().isBlank())
            drawPlatform.updateLogoImage(request.logoImage());
        return drawPlatform;
    }

    @Transactional
    public void deleteDrawPlatform(String name) {
        DrawPlatform drawPlatform = drawPlatformRepository.findByName(name)
                .orElseThrow(() -> new ApiException(DRAW_PLATFORM_NOT_FOUND));
        drawPlatformRepository.delete(drawPlatform);
    }
}
