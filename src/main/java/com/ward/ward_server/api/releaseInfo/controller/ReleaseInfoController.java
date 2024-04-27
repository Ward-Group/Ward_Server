package com.ward.ward_server.api.releaseInfo.controller;

import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoRequest;
import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoDetailResponse;
import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoSimpleResponse;
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

    @PostMapping
    public ApiResponse<ReleaseInfoDetailResponse> createReleaseInfo(@RequestBody ReleaseInfoRequest request) {
        return ApiResponse.ok(RELEASE_INFO_CREATE_SUCCESS, releaseInfoService.createReleaseInfo(request));
    }

    @GetMapping("/{itemCode}")
    public ApiResponse<List<ReleaseInfoSimpleResponse>> getReleaseInfoList(@PathVariable("itemCode") String itemCode){
        return ApiResponse.ok(RELEASE_INFO_LIST_LOAD_SUCCESS, releaseInfoService.getReleaseInfoList(itemCode));
    }

    @PatchMapping
    public ApiResponse<ReleaseInfoDetailResponse> updateReleaseInfo(@NotNull @RequestParam(value = "itemCode") String itemCode,
                                                                    @NotNull @RequestParam(value = "drawPlatform") String drawPlatform,
                                                                    @RequestBody ReleaseInfoRequest request) {
        return ApiResponse.ok(RELEASE_INFO_UPDATE_SUCCESS, releaseInfoService.updateReleaseInfo(itemCode, drawPlatform, request));
    }

    @DeleteMapping
    public ApiResponse deleteReleaseInfo(@NotNull @RequestParam(value = "itemCode") String itemCode,
                                           @NotNull @RequestParam(value = "drawPlatform") String drawPlatform){
        releaseInfoService.deleteReleaseInfo(itemCode, drawPlatform);
        return ApiResponse.ok(RELEASE_INFO_DELETE_SUCCESS);
    }

}
