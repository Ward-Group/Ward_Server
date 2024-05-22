package com.ward.ward_server.api.releaseInfo.controller;

import com.ward.ward_server.api.releaseInfo.dto.DrawPlatformRequest;
import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoDetailResponse;
import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoRequest;
import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoSimpleResponse;
import com.ward.ward_server.api.releaseInfo.entity.DrawPlatform;
import com.ward.ward_server.api.releaseInfo.service.DrawPlatformService;
import com.ward.ward_server.api.releaseInfo.service.ReleaseInfoService;
import com.ward.ward_server.global.response.ApiResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ward.ward_server.global.response.ApiResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/releaseInfos")
public class ReleaseInfoController {
    private final ReleaseInfoService releaseInfoService;
    private final DrawPlatformService drawPlatformService;

    //TODO 관리자 권한만
    @PostMapping
    public ApiResponse<ReleaseInfoDetailResponse> createReleaseInfo(@RequestBody ReleaseInfoRequest request) {
        return ApiResponse.ok(RELEASE_INFO_CREATE_SUCCESS, releaseInfoService.createReleaseInfo(request));
    }

    @GetMapping
    public ApiResponse<List<ReleaseInfoSimpleResponse>> getReleaseInfoList(@RequestParam(value = "itemCode") String itemCode,
                                                                           @RequestParam(value = "brandName") String brandName) {
        return ApiResponse.ok(RELEASE_INFO_LIST_LOAD_SUCCESS, releaseInfoService.getReleaseInfoList(itemCode, brandName));
    }

    @PatchMapping
    public ApiResponse<ReleaseInfoDetailResponse> updateReleaseInfo(@RequestParam(value = "itemCode") String itemCode,
                                                                    @RequestParam(value = "brandName") String brandName,
                                                                    @RequestParam(value = "platformName") String platformName,
                                                                    @RequestBody ReleaseInfoRequest request) {
        return ApiResponse.ok(RELEASE_INFO_UPDATE_SUCCESS, releaseInfoService.updateReleaseInfo(itemCode, brandName, platformName, request));
    }

    @DeleteMapping
    public ApiResponse deleteReleaseInfo(@RequestParam(value = "itemCode") String itemCode,
                                         @RequestParam(value = "brandName") String brandName,
                                         @RequestParam(value = "platformName") String platformName) {
        releaseInfoService.deleteReleaseInfo(itemCode, brandName, platformName);
        return ApiResponse.ok(RELEASE_INFO_DELETE_SUCCESS);
    }

    @PostMapping("/drawPlatforms")
    public ApiResponse<DrawPlatform> createDrawPlatform(@RequestBody DrawPlatformRequest request) {
        return ApiResponse.ok(DRAW_PLATFORM_CREATE_SUCCESS, drawPlatformService.createDrawPlatform(request));
    }

    @PatchMapping("/drawPlatforms/{platformName}")
    public ApiResponse<DrawPlatform> updateDrawPlatform(@PathVariable("platformName") String name,
                                                        @RequestBody DrawPlatformRequest request) {
        return ApiResponse.ok(DRAW_PLATFORM_UPDATE_SUCCESS, drawPlatformService.updateDrawPlatform(name, request));
    }

    @DeleteMapping("/drawPlatforms/{platformName}")
    public ApiResponse deleteDrawPlatform(@PathVariable("platformName") String name) {
        drawPlatformService.deleteDrawPlatform(name);
        return ApiResponse.ok(DRAW_PLATFORM_DELETE_SUCCESS);
    }
}
