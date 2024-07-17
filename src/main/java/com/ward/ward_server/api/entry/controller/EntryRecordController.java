package com.ward.ward_server.api.entry.controller;

import com.ward.ward_server.api.entry.dto.EntryCountResponse;
import com.ward.ward_server.api.entry.dto.EntryDetailResponse;
import com.ward.ward_server.api.entry.dto.EntryRecordRequest;
import com.ward.ward_server.api.entry.dto.EntryRecordResponse;
import com.ward.ward_server.api.entry.service.EntryRecordService;
import com.ward.ward_server.api.user.auth.security.CustomUserDetails;
import com.ward.ward_server.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.ward.ward_server.global.response.ApiResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/entry-records")
public class EntryRecordController {

    private final EntryRecordService entryRecordService;

    @PostMapping
    public ApiResponse<EntryRecordResponse> createEntryRecord(@AuthenticationPrincipal CustomUserDetails principal,
                                                              @RequestBody EntryRecordRequest request) {
        return ApiResponse.ok(ENTRY_RECORD_CREATE_SUCCESS, entryRecordService.createEntryRecord(principal.getUserId(), request.releaseInfoId()));
    }

    @GetMapping("/count")
    public ApiResponse<EntryCountResponse> getEntryCounts(@AuthenticationPrincipal CustomUserDetails principal) {
        return ApiResponse.ok(ENTRY_COUNTS_FETCH_SUCCESS, entryRecordService.getEntryCounts(principal.getUserId()));
    }

    @GetMapping("/release-infos/{releaseInfoId}")
    public ApiResponse<EntryRecordResponse> getEntryRecordListByItem(@AuthenticationPrincipal CustomUserDetails principal,
                                                                     @PathVariable("releaseInfoId") Long releaseInfoId) {
        return ApiResponse.ok(ENTRY_RECORD_LOAD_SUCCESS, entryRecordService.getEntryRecordByReleaseInfo(principal.getUserId(), releaseInfoId));
    }

    @DeleteMapping("/release-infos/{releaseInfoId}")
    public ApiResponse<Void> deleteEntryRecord(@AuthenticationPrincipal CustomUserDetails principal,
                                               @PathVariable("releaseInfoId") Long releaseInfoId) {
        entryRecordService.deleteEntryRecord(principal.getUserId(), releaseInfoId);
        return ApiResponse.ok(ENTRY_RECORD_DELETE_SUCCESS);
    }

    @GetMapping
    public ApiResponse<Page<EntryDetailResponse>> getEntryRecords(@AuthenticationPrincipal CustomUserDetails principal,
                                                                  @RequestParam("status") String status,
                                                                  @PageableDefault(size = 20) Pageable pageable) {
        return ApiResponse.ok(ENTRY_RECORD_LOAD_SUCCESS, entryRecordService.getEntryRecordsByStatus(principal.getUserId(), status, pageable));
    }
}
