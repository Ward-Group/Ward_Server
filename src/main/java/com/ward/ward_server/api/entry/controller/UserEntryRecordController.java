package com.ward.ward_server.api.entry.controller;

import com.ward.ward_server.api.entry.dto.EntryRecordRequest;
import com.ward.ward_server.api.entry.dto.EntryRecordResponse;
import com.ward.ward_server.api.entry.service.EntryRecordService;
import com.ward.ward_server.api.user.auth.security.CustomUserDetails;
import com.ward.ward_server.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.ward.ward_server.global.response.ApiResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/entry-records")
public class UserEntryRecordController {

    private final EntryRecordService entryRecordService;

    @PostMapping
    public ApiResponse<EntryRecordResponse> createEntryRecord(@AuthenticationPrincipal CustomUserDetails principal,
                                                              @RequestBody EntryRecordRequest request) {
        return ApiResponse.ok(ENTRY_RECORD_CREATE_SUCCESS, entryRecordService.createEntryRecord(principal.getUserId(), request.releaseInfoId()));
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
}
