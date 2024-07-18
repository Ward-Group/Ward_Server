package com.ward.ward_server.api.releaseInfo.controller;

import com.ward.ward_server.api.releaseInfo.dto.DrawPlatformRequest;
import com.ward.ward_server.api.releaseInfo.entity.DrawPlatform;
import com.ward.ward_server.api.releaseInfo.service.DrawPlatformService;
import com.ward.ward_server.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.ward.ward_server.global.response.ApiResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/draw-platforms")
public class DrawPlatformController {
    private final DrawPlatformService drawPlatformService;

    @PostMapping
    public ApiResponse<DrawPlatform> createDrawPlatform(@RequestBody DrawPlatformRequest request) {
        return ApiResponse.ok(DRAW_PLATFORM_CREATE_SUCCESS,
                drawPlatformService.createDrawPlatform(
                        request.koreanName() != null ? request.koreanName().toLowerCase() : null,
                        request.englishName() != null ? request.englishName().toLowerCase() : null,
                        request.logoImage()));
    }

    @PatchMapping("/{platformName}")
    public ApiResponse<DrawPlatform> updateDrawPlatform(@PathVariable("platformName") String originName,
                                                        @RequestBody DrawPlatformRequest request) {
        return ApiResponse.ok(DRAW_PLATFORM_UPDATE_SUCCESS, drawPlatformService.updateDrawPlatform(
                originName,
                request.koreanName() != null ? request.koreanName().toLowerCase() : null,
                request.englishName() != null ? request.englishName().toLowerCase() : null,
                request.logoImage()));
    }

    @DeleteMapping("/{platformName}")
    public ApiResponse<Void> deleteDrawPlatform(@PathVariable("platformName") String originName) {
        drawPlatformService.deleteDrawPlatform(originName);
        return ApiResponse.ok(DRAW_PLATFORM_DELETE_SUCCESS);
    }
}
