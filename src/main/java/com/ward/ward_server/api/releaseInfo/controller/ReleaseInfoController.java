package com.ward.ward_server.api.releaseInfo.controller;

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

    //TODO 관리자 권한만
    @PostMapping
    public ApiResponse<ReleaseInfo> createReleaseInfo(@RequestBody ReleaseInfoRequest request) {
        return ApiResponse.ok(RELEASE_INFO_CREATE_SUCCESS, releaseInfoService.createReleaseInfo(
                request.itemCode(),
                request.brandName(),
                request.platformName(),
                request.siteUrl(),
                request.releaseDate(),
                request.dueDate(),
                request.presentationDate(),
                request.notificationMethod(),
                request.releaseMethod(),
                request.deliveryMethod()));
    }

    @GetMapping
    public ApiResponse<ReleaseInfo> getReleaseInfo(@RequestParam(value = "itemCode") String itemCode,
                                                                           @RequestParam(value = "brandName") String brandName,
                                                                           @RequestParam(value = "platformName") String platformName) {
        return ApiResponse.ok(RELEASE_INFO_LIST_LOAD_SUCCESS, releaseInfoService.getReleaseInfo(itemCode, brandName, platformName));
    }

    @PatchMapping
    public ApiResponse<ReleaseInfo> updateReleaseInfo(@RequestParam(value = "originItemCode") String originItemCode,
                                                      @RequestParam(value = "originBrandName") String originBrandName,
                                                      @RequestParam(value = "originPlatformName") String originPlatformName,
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
                request.notificationMethod(),
                request.releaseMethod(),
                request.deliveryMethod()));
    }

    @DeleteMapping
    public ApiResponse<Void> deleteReleaseInfo(@RequestParam(value = "itemCode") String itemCode,
                                               @RequestParam(value = "brandName") String brandName,
                                               @RequestParam(value = "platformName") String platformName) {
        releaseInfoService.deleteReleaseInfo(itemCode, brandName, platformName);
        return ApiResponse.ok(RELEASE_INFO_DELETE_SUCCESS);
    }

}
