package com.ward.ward_server.api.releaseInfo.controller;

import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoDetailResponse;
import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoRequest;
import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoSimpleResponse;
import com.ward.ward_server.api.releaseInfo.entity.ReleaseInfo;
import com.ward.ward_server.api.releaseInfo.service.ReleaseInfoService;
import com.ward.ward_server.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ward.ward_server.global.response.ApiResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/release-infos")
public class ReleaseInfoController {
    private final ReleaseInfoService releaseInfoService;

    @PostMapping
    public ApiResponse<ReleaseInfoDetailResponse> createReleaseInfo(@RequestBody ReleaseInfoRequest request) {
        return ApiResponse.ok(RELEASE_INFO_CREATE_SUCCESS, releaseInfoService.createReleaseInfo(
                request.itemCode(),
                request.brandName(),
                request.platformName(),
                request.siteUrl(),
                request.releaseDate(),
                request.dueDate(),
                request.presentationDate(),
                request.releasePrice(),
                request.currencyUnit(),
                request.notificationMethod(),
                request.releaseMethod(),
                request.deliveryMethod()));
    }

    @GetMapping
    public ApiResponse<ReleaseInfoDetailResponse> getReleaseInfo(@RequestParam(value = "item-code") String itemCode,
                                                                 @RequestParam(value = "brand-name") String brandName,
                                                                 @RequestParam(value = "platform-name") String platformName) {
        return ApiResponse.ok(RELEASE_INFO_LIST_LOAD_SUCCESS, releaseInfoService.getReleaseInfo(itemCode, brandName, platformName));
    }

    @PatchMapping
    public ApiResponse<ReleaseInfoDetailResponse> updateReleaseInfo(@RequestParam(value = "origin-item-code") String originItemCode,
                                                      @RequestParam(value = "origin-brand-name") String originBrandName,
                                                      @RequestParam(value = "origin-platform-name") String originPlatformName,
                                                      @RequestBody ReleaseInfoRequest request) {
        return ApiResponse.ok(RELEASE_INFO_UPDATE_SUCCESS, releaseInfoService.updateReleaseInfo(
                originItemCode, originBrandName, originPlatformName,
                request.itemCode(),
                request.brandName(),
                request.platformName(),
                request.siteUrl(),
                request.releaseDate(),
                request.dueDate(),
                request.presentationDate(),
                request.releasePrice(),
                request.currencyUnit(),
                request.notificationMethod(),
                request.releaseMethod(),
                request.deliveryMethod()));
    }

    @DeleteMapping
    public ApiResponse<Void> deleteReleaseInfo(@RequestParam(value = "item-code") String itemCode,
                                               @RequestParam(value = "brand-name") String brandName,
                                               @RequestParam(value = "platform-name") String platformName) {
        releaseInfoService.deleteReleaseInfo(itemCode, brandName, platformName);
        return ApiResponse.ok(RELEASE_INFO_DELETE_SUCCESS);
    }

}
