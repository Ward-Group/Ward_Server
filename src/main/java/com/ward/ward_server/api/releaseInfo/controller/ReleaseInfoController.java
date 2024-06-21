package com.ward.ward_server.api.releaseInfo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoDetailResponse;
import com.ward.ward_server.api.releaseInfo.dto.ItemIdentifier;
import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoRequest;
import com.ward.ward_server.api.releaseInfo.service.ReleaseInfoService;
import com.ward.ward_server.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ApiResponse<ReleaseInfoDetailResponse> getReleaseInfo(@RequestBody ItemIdentifier request) {
        return ApiResponse.ok(RELEASE_INFO_LIST_LOAD_SUCCESS, releaseInfoService.getReleaseInfo(request.itemId(), request.platformName()));
    }

    @PatchMapping
    public ApiResponse<ReleaseInfoDetailResponse> updateReleaseInfo(@RequestBody ObjectNode node) throws JsonProcessingException {
        ObjectMapper mapper=new ObjectMapper();
        ItemIdentifier origin=mapper.treeToValue(node.get("itemIdentifier"), ItemIdentifier.class);
        ReleaseInfoRequest request=mapper.treeToValue(node.get("releaseInfoRequest"), ReleaseInfoRequest.class);
        return ApiResponse.ok(RELEASE_INFO_UPDATE_SUCCESS, releaseInfoService.updateReleaseInfo(
                origin.itemId(),
                origin.platformName(),
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

    @DeleteMapping
    public ApiResponse<Void> deleteReleaseInfo(@RequestBody ItemIdentifier request) {
        releaseInfoService.deleteReleaseInfo(request.itemId(), request.platformName());
        return ApiResponse.ok(RELEASE_INFO_DELETE_SUCCESS);
    }

}
