package com.ward.ward_server.api.releaseInfo.controller;

import com.ward.ward_server.api.item.entity.enums.Category;
import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoDetailResponse;
import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoRequest;
import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoSimpleResponse;
import com.ward.ward_server.api.releaseInfo.entity.ReleaseInfo;
import com.ward.ward_server.api.releaseInfo.service.ReleaseInfoService;
import com.ward.ward_server.api.user.auth.security.CustomUserDetails;
import com.ward.ward_server.global.Object.PageResponse;
import com.ward.ward_server.global.Object.enums.Section;
import com.ward.ward_server.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.index.qual.Positive;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ward.ward_server.global.response.ApiResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/release-infos")
@Slf4j
public class ReleaseInfoController {
    private final ReleaseInfoService releaseInfoService;

    @PostMapping
    public ApiResponse<ReleaseInfoDetailResponse> createReleaseInfo(@RequestBody ReleaseInfoRequest request) {
        return ApiResponse.ok(RELEASE_INFO_CREATE_SUCCESS, releaseInfoService.createReleaseInfo(
                request.itemId(),
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

    @GetMapping("/{releaseInfoId}")
    public ApiResponse<ReleaseInfoDetailResponse> getReleaseInfo(@PathVariable("releaseInfoId") Long releaseInfoId) {
        return ApiResponse.ok(RELEASE_INFO_DETAIL_LOAD_SUCCESS, releaseInfoService.getReleaseInfo(releaseInfoId));
    }

    @GetMapping("/{section}/home")
    public ApiResponse<List<ReleaseInfoSimpleResponse>> getReleaseInfo10List(@AuthenticationPrincipal CustomUserDetails principal,
                                                                             @PathVariable("section") Section section,
                                                                             @RequestParam("category") Category category) {
        return ApiResponse.ok(RELEASE_INFO_LIST_LOAD_SUCCESS, releaseInfoService.getReleaseInfo10List(principal.getUserId(), section, category));
    }

    @GetMapping("/{section}")
    public ApiResponse<PageResponse<ReleaseInfoSimpleResponse>> getReleaseInfoPage(@AuthenticationPrincipal CustomUserDetails principal,
                                                                                   @PathVariable("section") Section section,
                                                                                   @RequestParam("category") Category category,
                                                                                   @Positive @RequestParam(value = "page") int page) {
        return ApiResponse.ok(RELEASE_INFO_LIST_LOAD_SUCCESS, releaseInfoService.getReleaseInfoPage(principal.getUserId(), section, category, page - 1));
    }

    @GetMapping("/{itemId}/releases")
    public ApiResponse<Page<ReleaseInfo>> getReleaseInfos(
            @PathVariable("itemId") Long itemId,
            @RequestParam("status") String status,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        Page<ReleaseInfo> releaseInfos;
        if ("ongoing".equalsIgnoreCase(status)) {
            releaseInfos = releaseInfoService.getOngoingReleaseInfos(itemId, page, size);
        } else if ("completed".equalsIgnoreCase(status)) {
            releaseInfos = releaseInfoService.getCompletedReleaseInfos(itemId, page, size);
        } else {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
        return ApiResponse.ok(releaseInfos);
    }

    @PatchMapping("/{releaseInfoId}")
    public ApiResponse<ReleaseInfoDetailResponse> updateReleaseInfo(@PathVariable("releaseInfoId") Long releaseInfoId,
                                                                    @RequestBody ReleaseInfoRequest request) {
        return ApiResponse.ok(RELEASE_INFO_UPDATE_SUCCESS, releaseInfoService.updateReleaseInfo(
                releaseInfoId,
                request.itemId(),
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

    @DeleteMapping("/{releaseInfoId}")
    public ApiResponse<Void> deleteReleaseInfo(@PathVariable("releaseInfoId") Long releaseInfoId) {
        releaseInfoService.deleteReleaseInfo(releaseInfoId);
        return ApiResponse.ok(RELEASE_INFO_DELETE_SUCCESS);
    }

}
