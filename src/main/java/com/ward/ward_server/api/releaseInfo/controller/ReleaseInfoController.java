package com.ward.ward_server.api.releaseInfo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoDetailResponse;
import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoRequest;
import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoSimpleResponse;
import com.ward.ward_server.api.releaseInfo.service.ReleaseInfoService;
import com.ward.ward_server.api.user.auth.security.CustomUserDetails;
import com.ward.ward_server.global.Object.enums.Sort;
import com.ward.ward_server.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping
    public ApiResponse<List<ReleaseInfoSimpleResponse>> getReleaseInfo10List(@AuthenticationPrincipal CustomUserDetails principal,
                                                                             @RequestParam Sort sort) {
        return ApiResponse.ok(RELEASE_INFO_LIST_LOAD_SUCCESS, releaseInfoService.getReleaseInfo10List(principal.getUserId(), sort));
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
